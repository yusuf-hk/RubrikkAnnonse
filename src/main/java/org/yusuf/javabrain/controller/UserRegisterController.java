package org.yusuf.javabrain.controller;

import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.security.AuthenticationService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRegisterController
{
    private AuthenticationService authenticationService = new AuthenticationService();

    @PermitAll
    @POST
    public String registerUser(User user)
    {
        if(authenticationService.checkEmailExist(user.getEmail()))
        {
            return "The email is already in use, try another one";
        }
        else
        {
            authenticationService.registerUser(user);
            return "Successfully registered";
        }
    }
}
