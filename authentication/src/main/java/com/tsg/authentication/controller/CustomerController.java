package com.tsg.authentication.controller;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tsg.authentication.models.request.UpdateUser;
import com.tsg.authentication.models.response.CustomerResponse;
import com.tsg.authentication.models.response.UserListResponse;
import com.tsg.authentication.service.CustomerService;
import com.tsg.commons.exception.NotFoundException;
import com.tsg.commons.models.enums.GenericPage;
import com.tsg.commons.models.enums.ResponseDTO;

import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    
    @GetMapping("/get-all")
    public ResponseEntity<Page<UserListResponse>> getAllUsers(
			@RequestParam(value = "page-number") int pageNumber,
			@RequestParam(value = "page-size") int pageSize,
			@RequestParam(value = "sort-direction") String sortDirection) {
         try {
            GenericPage page = new GenericPage();
            page.setPageNumber(pageNumber);
            page.setPageSize(pageSize);
            page.setSortDirection(Sort.Direction.valueOf(sortDirection));
            Page<UserListResponse> pageReturn = customerService.findAllWithFiltersPageable(page);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(pageReturn);
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    @GetMapping("/get-details")
    public ResponseEntity<CustomerResponse> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(customerService.getUserById(id));
    }


    @PutMapping("/update-customer")
    public ResponseEntity<CustomerResponse> updateUser(@RequestParam Long id, @RequestBody @Valid UpdateUser userUpdateDTO) {
        return ResponseEntity.ok(customerService.updateUser(id, userUpdateDTO));
    }


    @DeleteMapping("/delete-customer")
    public ResponseEntity<ResponseDTO> deleteUser(@RequestParam Long id) {
        customerService.deleteUser(id);
         ResponseDTO response = ResponseDTO.builder().httpStatusCode(HttpStatus.ACCEPTED.value()).timestamp(LocalDateTime.now())
                        .description(Collections.singletonList("Se ha eliminado con exito.")).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
