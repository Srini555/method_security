package com.amisoftdemo.config;


import com.amisoftdemo.entity.Message;
import com.amisoftdemo.entity.User;
import org.springframework.stereotype.Service;

@Service ("authz")
public class AuthService {

    public boolean check(Message msg, User user){

        return msg.getTo().getId().equals(user.getId());
    }
}
