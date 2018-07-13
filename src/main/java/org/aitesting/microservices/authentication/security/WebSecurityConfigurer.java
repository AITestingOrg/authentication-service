package org.aitesting.microservices.authentication.security;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user where username=?")
                .authoritiesByUsernameQuery("SELECT u.username, r.description FROM user u, user_role ur, role r "
                        + "WHERE r.role_id=ur.role_id AND ur.user_id=u.user_id AND u.username=?");
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("auth/swagger-ui.html").antMatchers("auth/v2/api-docs");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .antMatcher("auth/swagger-ui.html").authorizeRequests().anyRequest().permitAll()
//                .and()
//                .antMatcher("auth/v2/api-docs").authorizeRequests().anyRequest().permitAll()
//                .and()
//                .antMatcher("v2/api-docs").authorizeRequests().anyRequest().permitAll()
//                .and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().permitAll();
//                .antMatchers("/**").permitAll();
//                .antMatchers("/v2/api-docs").permitAll()
//                .anyRequest().authenticated();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//        .antMatchers(HttpMethod.OPTIONS);
//    }

}
