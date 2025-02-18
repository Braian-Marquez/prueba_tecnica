package com.tsg.authentication.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.tsg.authentication.models.repository.UserRepository;
import com.tsg.authentication.models.request.AuthenticationRequest;
import com.tsg.authentication.models.request.LoginRequest;
import com.tsg.authentication.models.response.UserProfileResponse;
import com.tsg.authentication.models.response.UserProfileResponseImpl;
import com.tsg.authentication.security.JwtService;
import com.tsg.authentication.service.UserDetailsCustomService;
import com.tsg.authentication.utils.Messenger;
import com.tsg.commons.exception.BadCredentialsException;
import com.tsg.commons.exception.InvalidCredentialsException;
import com.tsg.commons.models.entity.UserEntity;
import com.tsg.commons.models.enums.CodeEnum;

import jakarta.validation.Valid;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/v1")
public class UserController {

	private final UserDetailsCustomService service;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final Messenger messenger;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequest authenticationRequest)
	        throws InvalidCredentialsException {
	    UserEntity userEntity = service.login(authenticationRequest);
	    if (userEntity != null) {
	    	  String jwtToken = jwtService.generateToken(userEntity);
	          UserProfileResponse userProfile = userRepository.findUserProfileByUserId(userEntity.getId())
	                  .map(profile -> new UserProfileResponseImpl(
	                          profile.getIdCustomer(),
	                          profile.getIdUser(),
	                          profile.getName(),
	                          profile.getRoles(),
	                          jwtToken)) 
	                  .orElseThrow(() -> new InvalidCredentialsException("El usuario no existe"));
	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfile);
	    } else {
	        throw new BadCredentialsException(messenger.getMessage(CodeEnum.BAD_CREDENTIALS));
	    }
	}


	@PostMapping("/register")
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid AuthenticationRequest user) throws IOException, InvalidCredentialsException {
		return service.saveCustomer(user);
	}

	
}
