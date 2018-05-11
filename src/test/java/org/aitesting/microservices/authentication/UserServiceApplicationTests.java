package org.aitesting.microservices.authentication;

import static org.junit.Assert.assertTrue;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserServiceApplicationTests {

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder().removeConflictingContainersOnStartup(true)
            .pullOnStartup(true).file("src/test/resources/docker-compose.yml")
            .waitingForService("mysql-server", HealthChecks.toHaveAllPortsOpen())
            .waitingForService("discovery-service", HealthChecks.toHaveAllPortsOpen()).build();

    @Autowired
    private JwtTokenStore tokenStore;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String tokenValue = "";

    @Before
    public void setUp() throws JSONException {
        String plainCreds = "front-end:front-end";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", "front-end");
        parameters.add("secret", "front-end");
        String body = "grant_type=password&scope=webclient&username=user1&password=password";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Retrieve token
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8091/auth/oauth/token", request,
                String.class, parameters);

        // extract tokenValue from response body
        JSONObject json = new JSONObject(response.getBody());
        tokenValue = json.getString("access_token");
    }

    @After
    public void tearDown() {
        docker.after();
    }

    /*
     * Verify that tokenValue is not empty
     */
    @Test
    public void getsTokenSuccess() {
        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        assertTrue(tokenValue != "");
        assertTrue(auth != null);
    }

    /*
     * Verify user status is authenticated
     */
    @Test
    public void tokenUserIsAuthenticatedSuccess() {
        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        assertTrue(auth.isAuthenticated());
    }

}
