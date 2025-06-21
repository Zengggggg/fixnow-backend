package com.fixnow.backend.services;

import com.fixnow.backend.dtos.request.EmailRequest;
import com.fixnow.backend.dtos.response.EmailResponse;

public interface EmailGenerationService {
    EmailResponse generateEmail(EmailRequest request);
}
