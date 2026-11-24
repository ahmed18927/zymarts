package com.example.SaaS.company.order.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Address is required")
    private Long addressId;

}
