package org.yusuf.javabrain.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.Logger;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.service.UserService;

import javax.ws.rs.core.Response;
import java.time.ZonedDateTime;
import java.util.Date;

public class AuthenticationService
{
    private final Logger LOGGER = Logger.getLogger(AuthenticationController.class);

    public String generateToken(User user)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_KEY);
            Date expirationDate = Date.from(ZonedDateTime.now().plusHours(24).toInstant());
            Date issuedAt = Date.from(ZonedDateTime.now().toInstant());
            return JWT.create()
                    .withIssuedAt(issuedAt) // Issue date.
                    .withExpiresAt(expirationDate) // Expiration date.
                    .withClaim("userId", user.getId()) // User id - here we can put anything we want, but for the example userId is appropriate.
                    .withIssuer("jwtauth") // Issuer of the token.
                    .sign(algorithm); // And the signing algorithm.
        } catch (JWTCreationException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public User validateToken(String token)
    {
        try
        {
            if (token != null)
            {
                Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_KEY);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("jwtauth")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);

                //Get the userId from token claim.
                Claim userId = jwt.getClaim("userId");

                // Find user by token subject(id).
                UserService userService = new UserService();
                return userService.findUserById(userId.asInt());
            }
        } catch (JWTVerificationException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public Response validateUserProps(User user)
    {
        ResponsePojo responsePojo = new ResponsePojo();
        if (user.getEmail() == null || user.getEmail().isEmpty())
        {
            responsePojo.setError("Email is required!");
            return Response.status(422).entity(responsePojo).build();
        }
        if (user.getPassword() == null || user.getPassword().isEmpty())
        {
            responsePojo.setError("Password is required!");
            return Response.status(422).entity(responsePojo).build();
        }
        return null;
    }
}
