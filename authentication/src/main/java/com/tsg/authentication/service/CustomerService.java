package com.tsg.authentication.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tsg.authentication.models.repository.CustomerRepository;
import com.tsg.authentication.models.repository.UserRepository;
import com.tsg.authentication.models.request.UpdateUser;
import com.tsg.authentication.models.response.CustomerResponse;
import com.tsg.authentication.models.response.UserListResponse;
import com.tsg.authentication.utils.Messenger;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.entity.Customer;
import com.tsg.commons.models.entity.UserEntity;
import com.tsg.commons.models.enums.CodeEnum;
import com.tsg.commons.models.enums.GenericPage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
 
    private static final CodeEnum CUSTOMER_NOT_EXIST = null;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final Messenger messenger;

    public Page<UserListResponse> findAllWithFiltersPageable(GenericPage page) {
		int offset = page.getPageNumber() * page.getPageSize();
		long totalElements = userRepository.findAll().size();
		List<UserListResponse> response = userRepository.findUserPage(page.getSortDirection().toString(), page.getPageSize(), offset);
		return new PageImpl<>(response, PageRequest.of(page.getPageNumber(), page.getPageSize(), page.getSortDirection(), page.getSortBy()),
				totalElements);
	}

    public CustomerResponse getUserById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer == null ) {
            throw new NotFoundException(messenger.getMessage(CUSTOMER_NOT_EXIST));
		}
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("El usuario no existe."));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setIdCustomer(customer.get().getId());
        customerResponse.setName(customer.get().getFirstName());
        customerResponse.setEmail(user.getEmail());
        customerResponse.setLastName(customer.get().getLastName());
        return customerResponse;

    }

    public void deleteUser(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer == null ) {
            throw new NotFoundException(messenger.getMessage(CUSTOMER_NOT_EXIST));
		}
        customerRepository.deleteById(id);
        userRepository.deleteById(customer.get().getIdUser());
    }

    public CustomerResponse updateUser(Long id, UpdateUser userUpdateDTO) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer == null ) {
            throw new NotFoundException(messenger.getMessage(CUSTOMER_NOT_EXIST));
		}
        customer.get().setFirstName(userUpdateDTO.getFirstName());
        customer.get().setLastName(userUpdateDTO.getLastName());
        customerRepository.save(customer.get());
        UserEntity user = userRepository.findById(customer.get().getIdUser()).orElseThrow(() -> new NotFoundException(messenger.getMessage(CUSTOMER_NOT_EXIST)));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setIdCustomer(customer.get().getId());
        customerResponse.setName(customer.get().getFirstName());
        customerResponse.setEmail(user.getEmail());
        customerResponse.setLastName(customer.get().getLastName());
        return customerResponse;
    }


    
}
