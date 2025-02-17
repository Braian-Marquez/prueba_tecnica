package com.tsg.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsg.authentication.models.repository.UserRepository;
import com.tsg.authentication.models.response.TokenValidationResponse;
import com.tsg.authentication.models.response.UserProfileResponse;
import com.tsg.authentication.security.JwtService;
import com.tsg.authentication.utils.Messenger;
import com.tsg.commons.exception.InvalidCredentialsException;
import com.tsg.commons.models.enums.CodeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/public/v1")
public class AuthorizedController {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final Messenger messenger;
	private Logger logger = LoggerFactory.getLogger(AuthorizedController.class);
	
	@Operation(hidden = true)
	@GetMapping(value = "/validate-token")
	public ResponseEntity<?> validateAuthToken(@RequestHeader("Authorization") String authHeader) {
	    try {
	        if (authHeader != null && !authHeader.isEmpty()) {
	            String jwtToken = authHeader; 
	            String username = jwtService.extractUsername(jwtToken);
	            System.out.println("username "+username);
	            UserProfileResponse userProfile = userRepository.findUserProfileByUsername(username).orElseThrow(() -> new InvalidCredentialsException("El usuario no existe"));
	            TokenValidationResponse validationResponse = new TokenValidationResponse();
	            validationResponse.setIdProfile(userProfile.getIdProfile());
	            validationResponse.setRoles(userProfile.getRoles());
	            logger.info("user: "+validationResponse.toString());
	            return ResponseEntity.status(HttpStatus.ACCEPTED).body(validationResponse);
	        } else {
	            throw new InvalidCredentialsException(messenger.getMessage(CodeEnum.INVALID_CREDENTIALS));
	        }
	    } catch (ExpiredJwtException ex) {
	        String errorMsg = "The provided token has expired.";
	        logger.error("Token expired: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	    } catch (JwtException ex) {
	        String errorMsg = "The token provided is invalid.";
	        logger.error("Invalid token: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	    } catch (Exception e) {
	        String errorMsg = "An unexpected error occurred.";
	        logger.error("Exception: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
	    }
	}



	
}
