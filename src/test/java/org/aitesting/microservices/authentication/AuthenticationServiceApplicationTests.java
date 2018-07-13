package org.aitesting.microservices.authentication;

import static org.junit.Assert.assertTrue;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AuthenticationServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AuthenticationServiceApplicationTests {

    protected static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceApplicationTests.class);

    @Autowired
    private TokenStore tokenStore;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private static String userServiceBaseURI = "http://localhost:8080";
    private String tokenValue = "";

    // Username and Password for App front-end
    final String appUsername = "front-end";
    final String appPassword = "front-end";

    // Set variables for access token request
    final String grantTypePassword = "password";
    final String scopeWeb = "webclient";
    final String usernamePassenger = "passenger";
    final String passwordPassenger = "password";

    // Start containers
    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder().removeConflictingContainersOnStartup(true)
            .shutdownStrategy(ShutdownStrategy.GRACEFUL).pullOnStartup(true)
            .file("src/test/resources/docker-compose.yml")
            .waitingForService("mysqlserver", HealthChecks.toHaveAllPortsOpen())
            .waitingForService("discoveryservice", HealthChecks.toHaveAllPortsOpen()).build();

    // Get token
    @Before
    public void setUp() throws JSONException {

        final String tokenURI = userServiceBaseURI + "/auth/oauth/token";

        String plainCreds = appUsername + ":" + appPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", appUsername);
        parameters.add("secret", appPassword);
        String body = "grant_type=" + grantTypePassword + "&scope=" + scopeWeb + "&username=" + usernamePassenger
                + "&password=" + passwordPassenger;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Retrieve token
        ResponseEntity<String> response = restTemplate.postForEntity(tokenURI, request, String.class, parameters);
        LOG.info("tokenURI: {}", tokenURI);
        LOG.info("request: {}", request);
        LOG.info("parameters: {}", parameters);
        LOG.info("response: {}", response.getBody());

        // extract tokenValue from response body
        JSONObject json = new JSONObject(response.getBody());
        tokenValue = json.getString("access_token");

        LOG.info("tokenValue: {}", tokenValue);
    }

    /*
     * Verify that tokenValue is not empty
     */
    @Test
    public void getsTokenSuccess() {
        assertTrue(!tokenValue.isEmpty());
    }

    /*
     * Verify user status is authenticated
     */
    @Test
    public void tokenPassengerIsAuthenticatedSuccess() throws JSONException {
        OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        assertTrue(auth.isAuthenticated());
    }

    /*
     * Get User Passenger Info
     */
    @Test
    public void getPassengerInfoSuccess() throws JSONException {

        final String tokenUserInfoURI = userServiceBaseURI + "/auth/user";

        assertTrue(!tokenValue.isEmpty());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenValue);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);

        // Retrieve token
        ResponseEntity<String> response = restTemplate.exchange(tokenUserInfoURI, HttpMethod.GET, httpEntity,
                String.class);

        // extract tokenValue from response body
        JSONObject json = new JSONObject(response.getBody());
        String username = json.getString("user");
        assertTrue(username.equals(usernamePassenger));
    }

}
