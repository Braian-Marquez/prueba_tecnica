package com.tsg.gateway.setups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalPreFiltering implements GlobalFilter {

	final Logger log =LoggerFactory.getLogger(GlobalPostFiltering.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange).doOnSuccess(v -> log.info("Global Pre Filter executed successfully."))
				.doOnError(e -> log.error("Error in Global Pre Filter execution", e));
    }
    
}


