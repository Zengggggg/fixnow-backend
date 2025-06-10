package com.fixnow.backend.services.impl;

import com.fixnow.backend.models.User;
import com.fixnow.backend.models.UserText;
import com.fixnow.backend.repositories.UserRepository;
import com.fixnow.backend.repositories.UserTextRepository;
import com.fixnow.backend.services.UserTextService;

import java.util.List;

public class UserTextServiceImpl implements UserTextService {

    private final UserRepository userRepository;

    private final UserTextRepository userTextRepository;

    public UserTextServiceImpl(UserTextRepository userTextRepository,
                               UserRepository userRepository
    ) {
        this.userTextRepository = userTextRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserText saveTextForUser(Long userId, String text) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserText userText = new UserText();
        userText.setText(text);
        userText.setUser(user);
        return userTextRepository.save(userText);
    }

    @Override
    public List<UserText> getTextsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getTexts();
    }
}
