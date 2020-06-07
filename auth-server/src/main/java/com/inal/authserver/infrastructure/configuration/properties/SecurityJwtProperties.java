package com.inal.authserver.infrastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("security.jwt")
public class SecurityJwtProperties {

    private Resource keyStore;
    private String keyStorePassword;
    private String keyPairAlias;
    private String keyPairPassword;
    private Integer expirationMs;

    public Resource getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(Resource keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPairAlias() {
        return keyPairAlias;
    }

    public void setKeyPairAlias(String keyPairAlias) {
        this.keyPairAlias = keyPairAlias;
    }

    public String getKeyPairPassword() {
        return keyPairPassword;
    }

    public void setKeyPairPassword(String keyPairPassword) {
        this.keyPairPassword = keyPairPassword;
    }

    public Integer getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(Integer expirationMs) {
        this.expirationMs = expirationMs;
    }

}
