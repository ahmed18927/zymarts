//package com.example.SaaS.company.DTOS;
//
//import lombok.Data;
//
//@Data
//public class CompanyCreateRequest {
//private String name;
//private String domain;
//private String logo;
//private Boolean isActive;
//}

package com.example.SaaS.company.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyCreateRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Domain is required")
    @Size(min = 3, max = 255, message = "Domain must be between 3 and 255 characters")
    @Pattern(
            regexp = "^[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?)*$",
            message = "Invalid domain format (e.g., example.com)"
    )
    private String domain;

    @Size(max = 500, message = "Logo URL must be less than 500 characters")
    private String logo;

    private Boolean isActive;
}