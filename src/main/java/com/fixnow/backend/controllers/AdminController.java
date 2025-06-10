package com.fixnow.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
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
}
