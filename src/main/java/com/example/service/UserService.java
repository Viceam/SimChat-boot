package com.example.service;

import com.example.pojo.User;

public interface UserService {
    boolean register(User user);
    User select(String name);
}
