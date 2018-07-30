package org.aitesting.microservices.authentication.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.aitesting.microservices.authentication.model.User;
import org.aitesting.microservices.authentication.model.UserOrganization;
import org.aitesting.microservices.authentication.repository.OrgUserRepository;
import org.aitesting.microservices.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class JWTTokenEnhancer implements TokenEnhancer {

    @Autowired
    private OrgUserRepository orgUserRepo;

    @Autowired
    private UserRepository userRepo;

    private UUID getOrgId(String username) {
        UserOrganization orgUser = orgUserRepo.findByUsername(username);
        return orgUser.getOrganizationId();
    }

    private UUID getUserId(String username) {
        User user = userRepo.findByUsername(username);
        return user.getUserId();
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        UUID orgId = getOrgId(authentication.getName());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        additionalInfo.put("uuid", getUserId(userDetails.getUsername()));
        additionalInfo.put("organizationId", orgId);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
