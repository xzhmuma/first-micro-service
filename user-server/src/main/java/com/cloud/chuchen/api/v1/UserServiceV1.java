package com.cloud.chuchen.api.v1;

import com.cloud.chuchen.user.UserRepository;
import com.cloud.chuchen.user.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ChuChen
 */
@Service
public class UserServiceV1 {

    @Autowired
    private UserRepository userRepository;

    //@Cacheable(value = "user", key = "#username") // Cannot get Jedis connection ?
    @HystrixCommand
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}

