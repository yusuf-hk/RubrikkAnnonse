package org.yusuf.javabrain.security;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Auth0AuthenticationProvider {

    @Produces
    public AuthenticationController authenticationController(Auth0AuthenticationConfig config) {
        JwkProvider jwkProvider = new JwkProviderBuilder(config.getDomain()).build();
        return AuthenticationController.newBuilder(config.getDomain(), config.getClientId(), config.getClientSecret())
                .withJwkProvider(jwkProvider)
                .build();
    }
}
