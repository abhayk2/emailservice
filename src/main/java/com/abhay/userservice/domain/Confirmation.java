package com.abhay.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@Data
@Entity
@Table(name="confimations")
public class Confirmation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id ;
    private String token ;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdAt ;
    @OneToOne(targetEntity = User.class,  fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


    public Confirmation (User user) {
        this.createdAt = LocalDateTime.now() ;
        this.token = UUID.randomUUID().toString() ;
        this.user = user;
    }
}
