package com.example.provider.service;

import com.example.rpc.api.IUserService;
import com.example.rpc.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    Environment environment;
    @Override
    public User getById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setName(environment.getProperty("rpc.server.config.port"));
        log.info(user.toString());
        return user;
    }

}
