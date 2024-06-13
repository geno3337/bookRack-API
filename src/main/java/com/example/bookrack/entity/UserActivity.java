package com.example.bookrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "UserActivity")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "applicationId")
    private int applicationId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "sessionId")
    private int sessionId;

    @Column(name = "action")
    private String action;

    @Column(name = "loggedTime")
    private Date loggedTime;

}
