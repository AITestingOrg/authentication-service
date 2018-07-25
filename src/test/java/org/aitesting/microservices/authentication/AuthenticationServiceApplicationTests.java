package org.aitesting.microservices.authentication;

import static org.junit.Assert.assertTrue;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.aitesting.microservices.authentication.model.User;
import org.aitesting.microservices.authentication.model.UserOrganization;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthenticationServiceApplicationTests {

    protected final Logger log = LoggerFactory.getLogger(AuthenticationServiceApplicationTests.class);

    @Autowired
    private TokenStore tokenStore;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private String tokenValue = "";
    private final String hostUrl = "http://localhost";

    // Username and Password for App front-end
    private final String appUsername = "front-end";
    private final String appPassword = "front-end";

    // Set variables for access token request
    private final String grantTypePassword = "password";
    private final String scopeWeb = "webclient";
    private final String usernamePassenger = "passenger";
    private final String passwordPassenger = "password";

    // Start containers
    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder().removeConflictingContainersOnStartup(true)
            .shutdownStrategy(ShutdownStrategy.GRACEFUL).pullOnStartup(true)
            .file("src/test/resources/docker-compose.yml")
            .waitingForService("mysqlserver", HealthChecks.toHaveAllPortsOpen()).build();

    // Get token
    @Before
    public void setUp() throws JSONException {

        final String tokenURI = createURLWithPort("/auth/oauth/token");

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
        log.info("RestTemplate: {}", restTemplate);
        log.info("Port: {}", port);
        ResponseEntity<String> response = restTemplate.exchange(tokenURI, HttpMethod.POST, request, String.class,
                parameters);
        log.info("tokenURI: {}", tokenURI);
        log.info("request: {}", request);
        log.info("parameters: {}", parameters);
        log.info("response: {}", response.getBody());

        // extract tokenValue from response body
        JSONObject json = new JSONObject(response.getBody());
        tokenValue = json.getString("access_token");
        log.info(response.getBody());
        log.info("tokenValue: {}", tokenValue);
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

        final String tokenUserInfoURI = createURLWithPort("/auth/user");

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

    @Test
    public void getUserObject() throws Exception {

        final String tokenUserInfoURI = createURLWithPort("/auth/user");

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
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        assertTrue(user.getUsername().equals(username));
        assertTrue(user.getPassword().equals(password));
    }

    @Test
    public void getUserOrganizationObject() throws Exception {

        final String tokenUserInfoURI = createURLWithPort("/auth/user");

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
        UserOrganization userOrganization = new UserOrganization();
        userOrganization.setUsername(username);
        assertTrue(userOrganization.getUsername().equals(username));
    }

    @Test
    public void getAuthorities() throws Exception {

        final String tokenUserInfoURI = createURLWithPort("/auth/user");

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
        JSONArray jsonArray = json.getJSONArray("authorities");
        List<String> authorities = new ArrayList<>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                authorities.add(jsonArray.get(i).toString());
            }
        }
        assertTrue(authorities.size() > 0);
    }

    private String createURLWithPort(String uri) {
        return hostUrl + ":" + port + uri;
    }

}
