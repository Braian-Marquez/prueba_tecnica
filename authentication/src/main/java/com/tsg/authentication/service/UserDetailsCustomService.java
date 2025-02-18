package com.tsg.authentication.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import com.tsg.authentication.models.repository.CustomerRepository;
import com.tsg.authentication.models.repository.RoleRepository;
import com.tsg.authentication.models.repository.UserRepository;
import com.tsg.authentication.models.request.AuthenticationRequest;
import com.tsg.authentication.models.request.LoginRequest;
import com.tsg.authentication.models.response.UserResponse;
import com.tsg.authentication.security.JwtService;
import com.tsg.authentication.utils.Messenger;
import com.tsg.commons.exception.InvalidCredentialsException;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.entity.Customer;
import com.tsg.commons.models.entity.UserEntity;
import com.tsg.commons.models.enums.CodeEnum;
import com.tsg.commons.models.enums.RoleType;

@Service
@Validated
@RequiredArgsConstructor
public class UserDetailsCustomService {

	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final Messenger messenger;
	private final JwtService jwtService;

	public UserEntity login(LoginRequest request) throws AuthenticationException, InvalidCredentialsException {
		UserEntity user = userRepository.findByEmailOrUsername(request.getEmail(), request.getPassword())
				.orElseThrow(() -> new InvalidCredentialsException(messenger.getMessage(CodeEnum.USERNAME_NOT_FOUND)));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException(messenger.getMessage(CodeEnum.WRONG_PASSWORD));
		}
		return user;
	}

@Transactional
public ResponseEntity<?> saveCustomer(AuthenticationRequest userRequest) throws IOException, InvalidCredentialsException {
    try {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userRequest.getEmail());

        if (existingUser.isPresent()) {
            throw new NotFoundException("El usuario ya existe.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(userRequest.getEmail());
        newUser.setUsername(userRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setRoles(new ArrayList<>(List.of(roleRepository.findByName(RoleType.USER.getFullRoleName()))));

        userRepository.save(newUser);

		Customer entity = new Customer();
		entity.setIdUser(newUser.getId());
		entity.setFirstName(formatName(userRequest.getFirstName()));
		entity.setLastName(formatName(userRequest.getLastName()));
		customerRepository.save(entity);

		UserResponse result = new UserResponse();
		result.setIdUser(newUser.getId());
		result.setIdProfile(entity.getId());
		result.setToken(jwtService.generateToken(newUser));

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);

	} catch (DataIntegrityViolationException e) {
        throw new NotFoundException("El usuario ya existe.");
    }
}

	private String formatName(String name) {
		if (name == null || name.isEmpty()) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

}
