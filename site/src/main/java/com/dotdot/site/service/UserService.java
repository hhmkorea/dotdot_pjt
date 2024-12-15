package com.dotdot.site.service;

import com.dotdot.site.model.User;
import com.dotdot.site.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // TO-DO : 패스워드 암호화

    @Transactional
    public User findMember(String username) {
        User user = userRepository.findAllByUsername(username);
//        User user = userRepository.findAllByUsername(username).orElseGet(() -> {
//            return new User();
//        });
        return user;
    }
}
