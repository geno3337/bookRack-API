package com.example.bookrack.security;

import com.example.bookrack.entity.User;
import com.example.bookrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user1 = userRepository.findByEmailAndIsActiveAndIsEmailVerified(username,true,true);
        if (user1==null){
            new RuntimeException("invalid email");
        }
        return new MyUserDetails(user1);
    }
}
