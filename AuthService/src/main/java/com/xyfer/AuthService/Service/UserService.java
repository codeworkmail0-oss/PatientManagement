package com.xyfer.AuthService.Service;

import com.xyfer.AuthService.Model.User;
import com.xyfer.AuthService.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository ;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email) ;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

}
