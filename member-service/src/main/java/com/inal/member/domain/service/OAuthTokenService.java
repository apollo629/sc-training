package com.inal.member.domain.service;

import com.inal.member.domain.vo.TokenVo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthTokenService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthTokenService.class);

    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    @Value("${authServer.header}")
    private String header;

    @Value("${authServer.serviceName}")
    private String authServerServiceName;

    @Value("${authServer.tokenUrl}")
    private String tokenUrl;

    public OAuthTokenService(RestTemplate restTemplate, EurekaClient eurekaClient) {
        this.restTemplate = restTemplate;
        this.eurekaClient = eurekaClient;
    }

    public TokenVo retrieveOauthToken(String username, String password){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", createBasicAuthHeader());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("scope", "memberService");
        map.add("username", username);
        map.add("password", password);

        logger.info("OAuth Server Rest Calling with username: {}", username);
        return retrieveTokenFromAuthServer(headers, map);
    }

    private String createBasicAuthHeader() {
        return "Basic " + Base64.encodeBase64String(("memberService" + ":" + "{noop}secret").getBytes());
    }

    private TokenVo retrieveTokenFromAuthServer(HttpHeaders headers, MultiValueMap<String, String> map) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(authServerServiceName, false);
        String baseUrl = instanceInfo.getHomePageUrl() + tokenUrl;

        ResponseEntity<OAuth2AccessToken> response = restTemplate.postForEntity(baseUrl, request, OAuth2AccessToken.class);
        OAuth2AccessToken accessToken = response.getBody();
        TokenVo tokenVo = new TokenVo();
        tokenVo.setAccessToken(accessToken.getValue());
        tokenVo.setExpiresIn(accessToken.getExpiresIn());
        tokenVo.setRefreshToken(accessToken.getRefreshToken().getValue());
        return tokenVo;
    }

}
