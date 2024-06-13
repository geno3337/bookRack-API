package com.example.bookrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "EmailQueue")
public class EmailQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "toEmail")
    private String toEmail;

    @Column(name = "fromEmail")
    private String fromEmail;

    @Column(name = "bccEmail")
    private String bccEmail;

    @Column(name = "subject")
    private String subject;

    @Column(name = "templateData")
    private String templateData;

    @Column(name = "templateName")
    private String templateName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "noOfAttempt")
    private int noOfAttempt;

    @Column(name = "queuedAt")
    private Date queuedAt;

    @Column(name = "processesAt")
    private Date processedAt;


}
