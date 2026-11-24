package com.example.SaaS.landPage;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSettingResponse {
    private String phone;
    private String email;
    private String lat;
    private String lng;
}
