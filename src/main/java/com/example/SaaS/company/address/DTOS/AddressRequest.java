package com.example.SaaS.company.address.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotBlank(message = "Address (Arabic) is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String addressAr;

    @NotBlank(message = "Address (English) is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String addressEn;

    @NotNull(message = "City id is required")
    @Positive(message = "City id must be positive")
    private Long cityId;

    @NotNull(message = "Country id is required")
    @Positive(message = "Country id must be positive")
    private Long countryId;

    @NotNull(message = "Is default is required")
    private Boolean isDefault;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;

    @Size(max = 500, message = "Note must be less than 500 characters")
    private String noteAr;

    @Size(max = 500, message = "Note must be less than 500 characters")
    private String noteEn;
}