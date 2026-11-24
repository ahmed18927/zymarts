package com.example.SaaS.company.favorite;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FavoriteResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String image;

    private BigDecimal price;
}
