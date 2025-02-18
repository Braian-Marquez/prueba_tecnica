package com.tsg.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsg.authentication.models.repository.CustomerRepository;
import com.tsg.authentication.models.repository.UserRepository;
import com.tsg.authentication.models.request.UpdateUser;
import com.tsg.authentication.models.response.CustomerResponse;
import com.tsg.authentication.service.CustomerService;
import com.tsg.authentication.utils.Messenger;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.entity.Customer;
import com.tsg.commons.models.entity.UserEntity;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Messenger messenger;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_UserExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setIdUser(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("john.doe@example.com");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        CustomerResponse response = customerService.getUserById(1L);

        assertEquals(1L, response.getIdCustomer());
        assertEquals("John", response.getName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(1L, response.getIdUser());
    }

    @Test
    public void testGetUserById_UserNotExists() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getUserById(1L));
    }

    @Test
    public void testDeleteUser_UserExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setIdUser(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("john.doe@example.com");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        customerService.deleteUser(1L);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testDeleteUser_UserNotExists() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.deleteUser(1L));
    }

    @Test
    public void testUpdateUser_UserExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setIdUser(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("john.doe@example.com");

        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        CustomerResponse response = customerService.updateUser(1L, updateUser);

        assertEquals(1L, response.getIdCustomer());
        assertEquals("Jane", response.getName());
        assertEquals("Smith", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(1L, response.getIdUser());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testUpdateUser_UserNotExists() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        UpdateUser updateUser = new UpdateUser();
        assertThrows(NotFoundException.class, () -> customerService.updateUser(1L, updateUser));
    }
}