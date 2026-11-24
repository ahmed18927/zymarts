package com.example.SaaS.company.address;

import com.example.SaaS.common.entity.Address;
import com.example.SaaS.company.address.DTOS.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address, String addressLine, String note ){
        if (address == null){
            return null;
        }


        return AddressResponse.builder()
                .id(address.getId())
                .customerId(address.getCustomerId() != null ? address.getCustomerId().getId() : null)
                .customerName(address.getCustomerId() != null ? address.getCustomerId().getName() : null)
                .address(addressLine)
                .cityId(address.getCityId() != null ? address.getCityId().getId() : null)
                .countryId(address.getCountryId() != null ? address.getCountryId().getId() : null)
                .isDefault(address.getIsDefault())
                .isActive(address.getIsActive())
                .note(note)
                .build();

    }
}
