package com.example.SaaS.website.main;

import java.util.List;

public class HomeResponse {
    private List<ContentBlock<?>> content;

    public List<ContentBlock<?>> getContent() {
        return content;
    }

    public void setContent(List<ContentBlock<?>> content) {
        this.content = content;
    }
}
