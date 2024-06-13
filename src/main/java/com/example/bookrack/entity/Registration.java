package com.example.bookrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Registration",schema = "book_rack")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "appId")
    private int appId;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "otp")
    private Integer otp;

    @Column(name = "RequestIdentifier")
    private String requestIdentifier;

    @Column(name = "isOtpActivated")
    private Boolean isOtpActivated;

    @Column(name = "otpGeneratedAt")
    private Date otpGeneratedAt;

    @Column(name = "otpExpiresAt")
    private Date otpExpiresAt;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }


}
