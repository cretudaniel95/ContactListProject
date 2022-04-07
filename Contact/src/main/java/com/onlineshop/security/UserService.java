package com.onlineshop.security;

import com.onlineshop.security.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserService {

    @Transactional(rollbackFor = Exception.class)
    void createUser(User user);

}
