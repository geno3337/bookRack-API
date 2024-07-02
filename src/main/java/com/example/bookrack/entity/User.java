package com.example.bookrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;


@Data
@Entity
@Component
@Table(name = "user" ,schema = "book_rack")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;


    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;


    @Column(name = "profileImage")
    private String profileImage;

    @NotEmpty
    @Column(name = "role")
    private String role;

    @Column(name = "isEmailVerified")
    private Boolean isEmailVerified;

    @Column(name = "passwordResetToken")
    private String passwordResetToken;

    @Column(name = "passwordResetTokenCreatedAt")
    private Date passwordResetTokenCreatedAt;

    @Column(name = "passwordResetTokenExpiredAt")
    private Date passwordResetTokenExpiredAt;

    @Column(name = "signedInUsing")
    private String signedInUsing;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

}
