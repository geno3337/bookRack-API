package com.example.bookrack.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "Books")
@Entity
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "bookName")
    private String bookName;

    @Column(name = "authorName")
    private String authorName;

    @Column(name = "price")
    private String price;

    @Column(name="category")
    private String category;

    @Column(name = "book")
    private String book;

    @Column(name = "bookPoster")
    private String bookPoster;

    @Column(name = "bookSize")
    private int bookSize;

    @Column(name = "pages")
    private int pages;

    @Column(name = "uploadedAt")
    private Date uploadedAt;

    @Column(name = "numberOfDownloads")
    private int numberOfDownloads;

}
