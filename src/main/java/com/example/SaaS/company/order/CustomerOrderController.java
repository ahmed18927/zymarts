package com.example.SaaS.company.order;

import com.example.SaaS.common.entity.Order;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.order.dto.CreateOrderRequest;
import com.example.SaaS.company.order.dto.OrderMapper;
import com.example.SaaS.company.order.dto.OrderResourceDTO;
import com.example.SaaS.security.CustomerDetails;
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
@RequestMapping("api/customer/order")
public class CustomerOrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    private CustomerDetails currentCustomer() {
        return (CustomerDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResourceDTO>> create(@Valid @RequestBody CreateOrderRequest request) {
        CustomerDetails customer = currentCustomer();
        Order order = orderService.createOrder(customer.getCustomerId(), customer.getCompanyId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order created successfully", orderMapper.toResource(order)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResourceDTO>> show(@PathVariable Long id) {
        CustomerDetails customer = currentCustomer();
        Order order = orderService.findOrderForCustomer(id, customer.getCustomerId());
        return ResponseEntity.ok(new ApiResponse<>(true, "retrieved successfully", orderMapper.toResource(order)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResourceDTO>>> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CustomerDetails customer = currentCustomer();
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResourceDTO> result = orderService
                .findOrdersForCustomer(customer.getCustomerId(), status, pageable)
                .map(orderMapper::toResource);

        return ResponseEntity.ok(new ApiResponse<>(true, "retrieved successfully", result));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<OrderResourceDTO>> pay(@PathVariable Long id) {
        CustomerDetails customer = currentCustomer();
        Order order = orderService.payOrder(id, customer.getCustomerId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Order paid successfully", orderMapper.toResource(order)));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResourceDTO>> cancel(@PathVariable Long id) {
        CustomerDetails customer = currentCustomer();
        Order order = orderService.cancelOrder(id, customer.getCustomerId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Order cancelled successfully", orderMapper.toResource(order)));
    }
}
