package com.abhay.userservice.service;

import com.abhay.userservice.domain.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    User saveUser (User user,String emailType);
    Boolean verifyToken(String token);
}
