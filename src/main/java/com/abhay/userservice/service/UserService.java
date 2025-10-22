package com.abhay.userservice.service;

import com.abhay.userservice.domain.User;

public interface UserService {
    User saveUser (User user);
    Boolean verifyToken(String token);
}
