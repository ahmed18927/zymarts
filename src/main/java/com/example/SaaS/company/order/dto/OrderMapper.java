package com.example.SaaS.company.order.dto;

import com.example.SaaS.common.entity.Order;
import com.example.SaaS.common.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResourceDTO toResource(Order order) {
        List<OrderItemResourceDTO> items = order.getItems() == null ? List.of() :
                order.getItems().stream().map(this::toItemResource).toList();

        OrderPriceResourceDTO price = order.getOrderPrice() == null ? null :
                OrderPriceResourceDTO.builder()
                        .subTotal(order.getOrderPrice().getSubTotal())
                        .discount(order.getOrderPrice().getDiscount())
                        .shippingPrice(order.getOrderPrice().getShippingPrice())
                        .totalPrice(order.getOrderPrice().getTotalPrice())
                        .build();

        return OrderResourceDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .customerId(order.getCustomerId().getId())
                .addressId(order.getAddressId().getId())
//                .storeId(order.getStoreId().getId())
                .items(items)
                .price(price)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResourceDTO toItemResource(OrderItem item) {
        return OrderItemResourceDTO.builder()
                .id(item.getId())
                .productId(item.getProductId().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
