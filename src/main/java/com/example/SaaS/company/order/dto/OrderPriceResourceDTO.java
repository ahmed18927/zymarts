package com.example.SaaS.company.order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderPriceResourceDTO {

    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal shippingPrice;
    private BigDecimal totalPrice;
}
