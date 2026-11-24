package com.example.SaaS.website.main;

public class ContentBlock<T> {
    private String key;
    private T items;
    private Boolean hasMore;

    public ContentBlock() {}

    public ContentBlock(String key, T items) {
        this.key = key;
        this.items = items;
    }

    public ContentBlock(String key, T items, Boolean hasMore) {
        this.key = key;
        this.items = items;
        this.hasMore = hasMore;
    }

    public String getKey() {
        return key;
    }

    public T getItems() {
        return items;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }
}
