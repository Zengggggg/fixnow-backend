package com.fixnow.backend.dtos.request;

import lombok.Data;

@Data
public class UserUpdateDto {
    // We might not want to allow username changes easily.
    private String field;
    private String value;

    // Getters và Setters
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
} 