package com.example.SaaS.superAdmin.employee;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.superAdmin.employee.dto.AdminEmployeeRequest;
import com.example.SaaS.superAdmin.employee.dto.AdminEmployeeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/employees")
public class AdminEmployeeController {
    private final AdminEmployeeService adminEmployeeService;
    private final AdminEmployeeMapper adminEmployeeMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminEmployeeResponse>> create(
            @Valid @ModelAttribute AdminEmployeeRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Employee saved = adminEmployeeService.createEmployee(request, image);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true, "Employee created successfully", adminEmployeeMapper.toResponse(saved)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminEmployeeResponse>> findById(@PathVariable Long id) {
        Employee employee = adminEmployeeService.getEmployeeById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Employee retrieved successfully", adminEmployeeMapper.toResponse(employee))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminEmployeeResponse>>> findAll(
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

        Page<AdminEmployeeResponse> response = adminEmployeeService
                .getAllEmployees(search, pageable)
                .map(adminEmployeeMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Employees retrieved successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminEmployeeService.deleteEmployee(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Employee deleted successfully", null)
        );
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminEmployeeResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute AdminEmployeeRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        Employee updated = adminEmployeeService.updateEmployee(id, request, image);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Employee updated successfully", adminEmployeeMapper.toResponse(updated))
        );
    }
}
