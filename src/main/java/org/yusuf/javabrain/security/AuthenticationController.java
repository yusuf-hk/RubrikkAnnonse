package org.yusuf.javabrain.security;


import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.service.UserService;

import java.time.ZonedDateTime;
import java.util.Date;

@Path("auth")
public class AuthenticationController
{
    private final Logger LOGGER = Logger.getLogger(AuthenticationController.class);

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user)
    {
        ResponsePojo responsePojo = new ResponsePojo();
        // Check if user email and password are presented.
        Response res = validateUserProps(user);
        if (res != null) return res;

        //Save user to database
        UserService userService = new UserService();
        boolean saveSuccess = userService.createUser(user);

        if (!saveSuccess)
        {
            responsePojo.setError("Email is already in use!");
            return Response.status(422).entity(responsePojo).build();
        }

        responsePojo.setToken(generateToken(user));

        return Response.ok().entity(responsePojo).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(User user)
    {
        ResponsePojo responsePojo = new ResponsePojo();

        // Check if user email and password are presented.
        Response res = validateUserProps(user);
        if (res != null) return res;

        // Find user by email.
        UserService userService = new UserService();
        User existingUser = userService.findUserByEmail(user.getEmail());

        if (existingUser == null)
        {
            responsePojo.setError("User doesn't exists.");
            return Response.ok().entity(responsePojo).build();
        }

        if (existingUser.getPassword().equals(CypherUtils.getSHA512SecurePassword(user.getPassword())))
        {
            responsePojo.setToken(generateToken(existingUser));
        } else
        {
            responsePojo.setError("Authentication failed.");
        }

        return Response.ok().entity(responsePojo).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response protectedResource(@HeaderParam("authorization") String token)
    {
        User user = validateToken(token);
        if (user != null)
        {
            user.setPassword(null);
            return Response.ok().entity(user).build();
        } else
        {
            ResponsePojo responsePojo = new ResponsePojo();
            responsePojo.setError("Invalid token.");
            return Response.ok().entity(responsePojo).build();
        }
    }

    private String generateToken(User user)
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

    private User validateToken(String token)
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
                return userService.findUserById(userId.asLong());
            }
        } catch (JWTVerificationException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private Response
    validateUserProps(User user)
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
