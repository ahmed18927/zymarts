package com.example.SaaS.company.order.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderResourceDTO {

    private Long id;
    private String status;
    private Long customerId;
    private Long addressId;
    private Long storeId;
    private List<OrderItemResourceDTO> items;
    private OrderPriceResourceDTO price;
    private Instant createdAt;
    private Instant updatedAt;
}
