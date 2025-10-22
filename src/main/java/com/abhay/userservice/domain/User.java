package com.abhay.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name="user")
public class User {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id ;
    private String name;
    private String email;
    private String password;
    private Boolean isEnabled;
}
