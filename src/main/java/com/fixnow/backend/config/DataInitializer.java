package com.fixnow.backend.config;

import com.fixnow.backend.models.Role;
import com.fixnow.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
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
    }
}

