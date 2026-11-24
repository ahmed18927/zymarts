package com.example.SaaS.company.customer;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.customer.DTOS.CustomerLoginRequest;
import com.example.SaaS.company.customer.DTOS.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final customerService customerService;
    private final CustomerMapper customerMapper;

//    @PostMapping
//    public ResponseEntity<ApiResponse<CustomerResponse>> create(
//            @Valid @RequestBody CustomerRequest request
//    ){
//        Customer create = customerService.create(request);
//        CustomerResponse response = customerMapper.toResponse(create);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse<>(true, "Customer created successfully", response));
//    }

@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<CustomerResponse>> create(
        @RequestParam("companyId") Long companyId,
        @RequestParam("name") String name,
        @RequestParam("phone") String phone,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam(value = "image", required = false) MultipartFile image
) {
    CustomerLoginRequest request = CustomerLoginRequest.builder()
            .companyId(companyId)
            .name(name)
            .phone(phone)
            .email(email)
            .password(password)
            .build();

    Customer created = customerService.create(request, image);
    CustomerResponse response = customerMapper.toResponse(created);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Customer created successfully", response));
}



    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getAll(
            @PageableDefault(size = 10) Pageable pageable){

        Page<CustomerResponse> response = customerService.getAllCustomers(pageable)
                .map(customerMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customers retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long id){
        Customer customer = customerService.getById(id);
        CustomerResponse response = customerMapper.toResponse(customer);

        return ResponseEntity.ok(
          new ApiResponse<>(true, "Customer retrieved successfully", response)
        );
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<CustomerResponse>> update(
//            @PathVariable Long id,
//            @Valid @RequestBody CustomerRequest request) {
//
//        Customer updated = customerService.update(id, request);
//        CustomerResponse response = customerMapper.toResponse(updated);
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Customer updated successfully", response)
//        );
//    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @RequestParam("companyId") Long companyId,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        CustomerLoginRequest request = CustomerLoginRequest.builder()
                .companyId(companyId)
                .name(name)
                .phone(phone)
                .email(email)
                .password(password)
                .build();

        Customer updated = customerService.update(id, request, image);
        CustomerResponse response = customerMapper.toResponse(updated);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        customerService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer deleted successfully", null)
        );
    }
}
