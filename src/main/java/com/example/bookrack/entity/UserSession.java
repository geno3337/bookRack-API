package com.example.bookrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "UserSession")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "applicationId")
    private int applicationId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "refreshKey")
    private String refreshKey;

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name = "refreshKeyCreatedAt")
    private Date refreshKeyCreatedAt;

    @Column(name = "refreshKeyExpiredAt")
    private Date refreshKeyExpiredAt;

    @Column(name = "isRefreshKeyActive")
    private Boolean isRefreshKeyActive;

    @Column(name = "accessTokenIdentifier")
    private String accessTokenIdentifier;

    @Column(name = "accessTokenCreatedAt")
    private Date accessTokenCreatedAt;

    @Column(name = "accessTokenExpiresAt")
    private Date accessTokenExpiresAt;

    @Column(name = "isAccessTokenActive")
    private Boolean isAccessTokenActive;

    @Column(name = "loggedInUsing")
    private String loggedInUsing;

    @Column(name = "LastActivityAt")
    private Date LastActivityAt;
}
