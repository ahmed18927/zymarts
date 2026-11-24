package com.example.SaaS.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GuestTokenUtil {

    public static final String HEADER_NAME = "X-Guest-Token";

    public String generate(){
        return "guest_" + UUID.randomUUID();
    }
}
