package com.imbuka.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data

@NoArgsConstructor
@Entity

@Table(name = "confirmations")
public class Confirmation {

    @Id
    @GeneratedValue
    private Long id;
    /*
    what we will be sending to the user as their confirmation
    The token will be needed to look up for their info in the db
     */
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createDate;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Confirmation(User user) {
        this.user = user;
        this.createDate = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

}
