package org.aitesting.microservices.authentication;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.aitesting.microservices.authentication.model.User;
import org.aitesting.microservices.authentication.model.UserOrganization;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthenticationServiceUnitTests {

    private static UUID userId;
    private static UUID organizationId;
    private static String username;
    private static String password;
    private static Collection<? extends GrantedAuthority> authorities;

    @BeforeClass
    public static void tearUp() {
        userId = UUID.randomUUID();
        organizationId = UUID.randomUUID();
        username = "passenger";
        password = "password";
        authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_PASSENGER"));
    }

    @AfterClass
    public static void tearDown() {
        userId = null;
        organizationId = null;
        username = null;
        password = null;
        authorities = null;
    }

    @Test
    public void testUserOrganization() throws Exception {
        UserOrganization userOrg = new UserOrganization();
        userOrg.setUsername(username);
        userOrg.setOrganizationId(organizationId);

        assertTrue(userOrg.getOrganizationId().equals(organizationId));
        assertTrue(userOrg.getUsername().equals(username));
    }

    @Test
    public void testUser() throws Exception {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);

        assertTrue(user.getUserId().equals(userId));
        assertTrue(user.getUsername().equals(username));
        assertTrue(user.getPassword().equals(password));
    }

}
