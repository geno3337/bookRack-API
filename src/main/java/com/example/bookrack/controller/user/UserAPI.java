package com.example.bookrack.controller.user;

import com.example.bookrack.entity.User;
import com.example.bookrack.response.common.ApiResponse;
import com.example.bookrack.security.MyUserDetails;
import com.example.bookrack.utilities.ActiveUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserAPI {

    @Autowired
    private MyUserDetails myUserDetails;

    @GetMapping("/loadProfile")
    public ResponseEntity<ApiResponse> loadProfile(){
        ActiveUser activeUser = new ActiveUser();
         ApiResponse response =new ApiResponse("success","loadprofile",200);
         response.setUserDetails(activeUser);
         System.out.println(activeUser);
         return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }
}
