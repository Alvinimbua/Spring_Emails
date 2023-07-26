package com.imbuka.userservice.service;

import com.imbuka.userservice.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User saveUser(User user);
    Boolean verifyToken(String token);
}
