package com.fixnow.backend.controllers;

import com.fixnow.backend.models.TopUpRequest;
import com.fixnow.backend.repositories.TopUpRequestRepository;
import com.fixnow.backend.services.TopupRequestService;
import com.fixnow.backend.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor


public class AdminController {

    private final TopUpRequestRepository requestRepo;
    private final TopupRequestService requestService;
    private final WalletService walletService;

    @GetMapping("/admin/user")
    public String showUserPage(Model model) {
        return "admin/user";
    }
    @GetMapping("/admin/user/create")
    public String showCreateUserPage(Model model) {
        return "admin/user-create";
    }
    @GetMapping("/admin/user/table-users")
    public String showTableUsersPage(Model model) {
        return "admin/table-users";
    }


    @GetMapping
    public String showAllTopUps(Model model) {
        List<TopUpRequest> allTopUps = requestRepo.findAll(Sort.by(Sort.Direction.DESC, "requestTime"));
        model.addAttribute("topups", allTopUps);
        return "features/all-topups";
    }
    @PostMapping("/confirm")
    public String confirmTopup(@RequestParam("topupId") Long topupId) {
        requestService.confirmTopupRequest(topupId);
        return "redirect:/admin"; // Adjust if your view path differs
    }


}
