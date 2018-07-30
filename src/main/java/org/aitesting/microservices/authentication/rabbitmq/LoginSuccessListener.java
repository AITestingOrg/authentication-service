package org.aitesting.microservices.authentication.rabbitmq;

import org.aitesting.microservices.authentication.model.User;
import org.aitesting.microservices.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger log = LoggerFactory.getLogger(LoginSuccessListener.class);

    @Autowired
    Producer producer;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent evt) {
        if (evt.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) evt.getAuthentication().getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername());
            log.info(String.format("User %s logged in", user.getUserId()));
            try {
                producer.send(String.format("{\"userId\":\"%s\"}", user.getUserId()));
            } catch (Exception e) {
                log.error("Message not sent to Notification Service");
            }
        }
    }
}
