package com.example.SaaS.company.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "PENDING|PAID|SHIPPED|CANCELLED|COMPLETED",
            message = "Status must be PENDING, PAID, SHIPPED, CANCELLED, or COMPLETED"
    )
    private String status;
}
