package com.example.SaaS.company.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryMinResponse {
    private Long id;
    private String name;
}
