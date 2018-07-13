package org.aitesting.microservices.authentication;

import static org.junit.Assert.assertTrue;

import org.aitesting.microservices.authentication.model.User;
import org.aitesting.microservices.authentication.model.UserOrganization;
import org.junit.Test;

public class AuthenticationServiceUnitTests {

    @Test
    public void testUserOrganization() throws Exception {
        UserOrganization userOrg = new UserOrganization();
        userOrg.setUsername("user1");
        userOrg.setOrganizationId("00000");
        
        assertTrue(userOrg.getOrganizationId().equals("00000"));
        assertTrue(userOrg.getUsername().equals("user1"));
    }
    
    @Test
    public void testUser() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("user1");
        user.setPassword("password");
        
        assertTrue(user.getUserId() != null);
        assertTrue(user.getUsername() != null);
        assertTrue(user.getPassword() != null);
    }

}
