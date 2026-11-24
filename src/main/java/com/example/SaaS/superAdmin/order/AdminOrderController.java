package com.example.SaaS.superAdmin.order;

import com.example.SaaS.common.entity.Order;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.order.OrderService;
import com.example.SaaS.company.order.dto.OrderMapper;
import com.example.SaaS.company.order.dto.OrderResourceDTO;
import com.example.SaaS.company.order.dto.UpdateOrderStatusRequest;
import com.example.SaaS.security.EmployeeDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/order")
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private Long currentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResourceDTO>>> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResourceDTO> result = orderService
                .findAllOrdersForSuperAdmin(status, pageable)
                .map(orderMapper::toResource);

        return ResponseEntity.ok(new ApiResponse<>(true, "retrieved successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResourceDTO>> show(@PathVariable Long id) {
        Order order = orderService.findOrderForSuperAdmin(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "retrieved successfully", orderMapper.toResource(order)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResourceDTO>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        Order order = orderService.updateOrderStatusForSuperAdmin(id, request.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(true, "Order updated successfully", orderMapper.toResource(order)));
    }


}