package com.tsg.gateway.setups;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsg.gateway.response.UserInfoResponse;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UsuarioAuthorizationFilter extends AbstractGatewayFilterFactory<UsuarioAuthorizationFilter.Config> {

	private DiscoveryClient discoveryClient;

	public UsuarioAuthorizationFilter(DiscoveryClient discoveryClient, WebClient.Builder webclientBuilder) {
		super(Config.class);
		this.discoveryClient = discoveryClient;

	}

	@Override
	public GatewayFilter apply(Config config) {

		return new OrderedGatewayFilter((exchange, chain) -> {

			String requestPath = exchange.getRequest().getURI().getPath();
			if (requestPath.contains("/v3/api-docs")) {
				return chain.filter(exchange);
			}

			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				try {
					return unauthorizedResponse("Missing Authorization header", exchange.getResponse());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				try {
					return unauthorizedResponse("Bad Authorization structure", exchange.getResponse());
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			String token = authHeader; 
			return WebClient.builder()
					.baseUrl(getUrl() + "/user/v1/info")
					.defaultHeader(HttpHeaders.AUTHORIZATION, token)
					.build()
					.get()
					.retrieve()
					.bodyToMono(UserInfoResponse.class)
					.flatMap(response -> {
						if (response != null) {
							Long idUser = response.getId();
							exchange.getRequest().mutate().header("user-id", String.valueOf(idUser));

							List<String> roleList = response.getRoles();
							List<String> requiredRoles = List.of("ROLE_USER","ROLE_ADMIN");

							if (roleList.isEmpty() || roleList.stream().noneMatch(requiredRoles::contains)) {
								try {
									return unauthorizedResponse("No cuenta con el rol necesario",
											exchange.getResponse());
								} catch (JsonProcessingException e) {
									e.printStackTrace();
								}
							}
						} else {
							try {
								return unauthorizedResponse("Roles missing", exchange.getResponse());
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						}
						return chain.filter(exchange);
					})
					.onErrorResume(WebClientResponseException.class, error -> {
						// Manejo de errores de WebClient
						System.out.println("Error al llamar al servicio de autenticación: " + error.getStatusCode());
						System.out.println("Cuerpo del error: " + error.getResponseBodyAsString());
						try {
							return handleError(error, exchange.getResponse());
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						return null;
					});
		}, 1);
	}

	private ObjectMapper objectMapper = new ObjectMapper();

	private Mono<Void> unauthorizedResponse(String message, ServerHttpResponse response)
			throws JsonProcessingException {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> errorResponse = createErrorResponse(HttpStatus.FORBIDDEN.value(),
				"No cuenta con los permisos o roles necesarios");
		DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
		return response.writeWith(Mono.just(buffer));
	}

	private Mono<Void> handleError(WebClientResponseException error, ServerHttpResponse response)
			throws JsonProcessingException {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> errorResponse = createErrorResponse(status.value(), "Token invalido o caducado");
		DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
		return response.writeWith(Mono.just(buffer));
	}

	private Map<String, Object> createErrorResponse(int httpStatusCode, String description) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("httpStatusCode", httpStatusCode);
		errorResponse.put("description", List.of(description));
		errorResponse.put("timestamp", getCurrentTimestamp());

		Map<String, Object> response = new HashMap<>();
		response.put("error", errorResponse);

		return response;
	}

	private String getCurrentTimestamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");
		return now.format(formatter);
	}

	public static class Config {

	}

	public String getUrl() {
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("tsg-auth");
		if (!serviceInstances.isEmpty()) {
			ServiceInstance serviceInstance = serviceInstances.get(0);
			String serviceUrl = serviceInstance.getUri().toString();
			System.out.println("Service URL: " + serviceUrl);
			return serviceUrl;

		}
		System.out.println("No se encontro el servicio permisos-auth");
		return null;
	}

}
