package com.imbuka.userservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean isEnabled;

}
