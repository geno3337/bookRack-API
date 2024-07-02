package com.example.bookrack.pojo;

import lombok.Data;

@Data
public class GitHubUser {

    private int id;

    private String name;

    private String email;

    private String avatar_url;

    private String location;

    private Boolean isEmailVerified;
}
