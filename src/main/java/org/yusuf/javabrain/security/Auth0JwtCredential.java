package org.yusuf.javabrain.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.security.enterprise.credential.Credential;

public class Auth0JwtCredential implements Credential
{
    private Auth0JwtPrincipal auth0JwtPrincipal;

    Auth0JwtCredential(String token)
    {
        DecodedJWT decodedJWT = JWT.decode(token);
        this.auth0JwtPrincipal = new Auth0JwtPrincipal(decodedJWT);
    }

    Auth0JwtPrincipal getAuth0JwtPrincipal()
    {
        return auth0JwtPrincipal;
    }
}