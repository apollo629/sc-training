package com.inal.authserver.infrastructure.configuration;

import com.inal.authserver.domain.service.UserDetailsImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Map<String, Object> additionalInformation = new HashMap<>();
        UserDetailsImpl currentMemberUser = (UserDetailsImpl) principal;
        additionalInformation.put("tokenType", "member");
        additionalInformation.put("memberId", currentMemberUser.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return super.enhance(accessToken, authentication);
    }
}
