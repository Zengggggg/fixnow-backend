package com.fixnow.backend.services;

import com.fixnow.backend.models.UserText;

import java.util.List;

public interface UserTextService {
    UserText saveTextForUser(Long userId, String text);
    List<UserText> getTextsByUser(Long userId);
}
