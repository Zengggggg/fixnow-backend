package com.fixnow.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., FREEMIUM, BASIC, PREMIUM

    private String description;

    private Double price;

    @OneToMany(mappedBy = "role")
    private List<User> users;

}
