package com.example.service.impl;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public boolean register(User user) {
        if(user.getUsername() != null && user.getPassword() != null) {
            User user1 = select(user.getUsername());
            if(user1 == null) {
                int i = mapper.save(user);
                if(i > 0) return true;
            }
        }
        return false;
    }

    @Override
    public User select(String name) {
        User user = mapper.selectByUserName(name);
        return user;
    }
}
