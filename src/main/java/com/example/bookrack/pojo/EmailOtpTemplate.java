package com.example.bookrack.pojo;

import lombok.Data;

@Data
public class EmailOtpTemplate {

    private String name;

    private String otp;

    public void setData(String name, String otp) {
        setOtp(otp);
        setName(name);
    }
}
