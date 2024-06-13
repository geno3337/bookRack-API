package com.example.bookrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "AllowedApp")
public class AllowedApp {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "applicationKey")
    private String applicationKey;

    @Column(name = "applicationType")
    private String applicationType;

    @Column(name = "applicationDescription")
    private String applicationDescription;

    @Column(name = "isActive")
    private Boolean isActive;

}
