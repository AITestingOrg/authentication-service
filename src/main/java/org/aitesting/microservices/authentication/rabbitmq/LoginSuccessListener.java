package org.aitesting.microservices.authentication.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent>{

    private static final Logger log = LoggerFactory.getLogger(LoginSuccessListener.class);

    @Autowired
    Producer producer;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent evt) {
        if (evt.getSource() instanceof UsernamePasswordAuthenticationToken) {
            User user = (User) evt.getAuthentication().getPrincipal();
            log.info(String.format("User %s logged in", user.getUsername()));
            producer.send(String.format("{\"userId\":\"%s\"}", user.getUsername()));
        }
    }
}
