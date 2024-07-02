package com.example.bookrack.pojo;

import lombok.Data;

@Data
public class GoogleUser  {

    private int id;

    private String given_name;

    private String family_name;

    private String name;

    private String picture;

    private String locale;

    private String email;

    private Boolean email_verified;

}
