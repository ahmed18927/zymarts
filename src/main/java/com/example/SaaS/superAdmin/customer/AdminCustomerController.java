package com.example.SaaS.superAdmin.customer;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.superAdmin.customer.dto.AdminCustomerRequest;
import com.example.SaaS.superAdmin.customer.dto.AdminCustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;
    private final AdminCustomerMapper adminCustomerMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminCustomerResponse>> create(
            @RequestParam("companyId") Long companyId,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        AdminCustomerRequest request = AdminCustomerRequest.builder()
                .companyId(companyId)
                .name(name)
                .phone(phone)
                .email(email)
                .password(password)
                .build();

        Customer created = adminCustomerService.createCustomer(request, image);
        AdminCustomerResponse response = adminCustomerMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Customer created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminCustomerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AdminCustomerResponse> response = adminCustomerService
                .getAllCustomers(search, pageable)
                .map(adminCustomerMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customers retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminCustomerResponse>> getById(@PathVariable Long id) {
        Customer customer = adminCustomerService.getCustomerById(id);
        AdminCustomerResponse response = adminCustomerMapper.toResponse(customer);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer retrieved successfully", response)
        );
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminCustomerResponse>> update(
            @PathVariable Long id,
            @RequestParam("companyId") Long companyId,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        AdminCustomerRequest request = AdminCustomerRequest.builder()
                .companyId(companyId)
                .name(name)
                .phone(phone)
                .email(email)
                .password(password)
                .build();

        Customer updated = adminCustomerService.updateCustomer(id, request, image);
        AdminCustomerResponse response = adminCustomerMapper.toResponse(updated);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminCustomerService.deleteCustomer(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer deleted successfully", null)
        );
    }
}
