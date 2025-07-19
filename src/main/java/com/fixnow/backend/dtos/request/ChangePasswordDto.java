package com.fixnow.backend.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
