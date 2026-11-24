package com.example.SaaS.company.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRequest {

    @NotNull(message = "Product is required")
    private Long productId;
}
