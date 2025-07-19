package com.fixnow.backend.config;

import com.fixnow.backend.models.DiscountCode;
import com.fixnow.backend.models.Role;
import com.fixnow.backend.models.User;
import com.fixnow.backend.mun.Provider;
import com.fixnow.backend.repositories.DiscountCodeRepository;
import com.fixnow.backend.repositories.RoleRepository;
import com.fixnow.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Override
    public void run(String... args) {
        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
        }

        if (!roleRepository.findByName("ROLE_FREE").isPresent()) {
            Role role = new Role();
            role.setName("ROLE_FREE");
            roleRepository.save(role);
        }

        if (!roleRepository.findByName("ROLE_BASIC").isPresent()) {
            Role role = new Role();
            role.setName("ROLE_BASIC");
            roleRepository.save(role);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(
                    () -> new RuntimeException("No admin role found")
            );

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@confirm.com");
            admin.setPassword(passwordEncoder.encode("CungKhongBanLam"));
            admin.setEnabled(true);
            admin.setProvider(Provider.LOCAL);
            admin.setRole(adminRole);
            userRepository.save(admin);
            System.out.println("✅ Admin user created!");
        }
        if (!discountCodeRepository.findById("GIAM10").isPresent()) {
            DiscountCode code = new DiscountCode();
            code.setCode("GIAM10");
            code.setPercent(10);
            code.setActive(true);
            code.setExpiryDate(LocalDateTime.now().plusDays(30));
            discountCodeRepository.save(code);
        }

        if (!discountCodeRepository.findById("GIAM20").isPresent()) {
            DiscountCode code = new DiscountCode();
            code.setCode("GIAM20");
            code.setPercent(20);
            code.setActive(true);
            code.setExpiryDate(LocalDateTime.now().plusDays(15));
            discountCodeRepository.save(code);
        }

        if (!discountCodeRepository.findById("FIXNOW50").isPresent()) {
            DiscountCode code = new DiscountCode();
            code.setCode("FIXNOW50");
            code.setPercent(50);
            code.setActive(true);
            code.setExpiryDate(LocalDateTime.now().plusDays(7));
            discountCodeRepository.save(code);
        }

        System.out.println("✅ Discount codes seeded!");
    }

}

