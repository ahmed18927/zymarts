package com.example.SaaS.company.address.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse{
    private Long id;
    private Long customerId;
    private String customerName;
    private String address;
    private Long cityId;
    private Long countryId;
    private Boolean isDefault;
    private Boolean isActive;
    private String note;
}
