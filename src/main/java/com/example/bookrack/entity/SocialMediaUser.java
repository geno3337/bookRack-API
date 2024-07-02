package com.example.bookrack.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "socialMediaUser")
public class SocialMediaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "socialId")
    private String socialEmail;

    @Column(name = "userId")
    private int userId;

    @Column(name = "source")
    private String source;
}
