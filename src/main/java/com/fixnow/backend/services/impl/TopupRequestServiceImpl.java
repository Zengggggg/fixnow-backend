package com.fixnow.backend.services.impl;


import com.fixnow.backend.models.TopUpRequest;
import com.fixnow.backend.repositories.TopUpRequestRepository;
import com.fixnow.backend.services.TopupRequestService;
import com.fixnow.backend.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TopupRequestServiceImpl implements TopupRequestService {

    private final TopUpRequestRepository topupRequestRepository;
    private final WalletService walletService;

    @Override
    public void confirmTopupRequest(Long id) {
        TopUpRequest request = topupRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu nạp tiền với ID: " + id));

        if (request.isConfirmed()) {
            throw new IllegalStateException("Yêu cầu này đã được xác nhận.");
        }

        request.setConfirmed(true);
        request.setConfirmedTime(LocalDateTime.now());

        topupRequestRepository.save(request);

        walletService.topUp(request.getUser(), request.getAmount());
    }
}
