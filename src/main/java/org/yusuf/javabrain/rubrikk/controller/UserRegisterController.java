package org.yusuf.javabrain.rubrikk.controller;

import org.yusuf.javabrain.rubrikk.model.User;
//import org.yusuf.javabrain.security.oldsecurity.AuthenticationService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRegisterController
{

    //private AuthenticationService authenticationService = new AuthenticationService();

    @PermitAll
    @POST
    public String registerUser(User user)
    {
        return "Nice";
        /**
        if(authenticationService.checkEmailExist(user.getEmail()))
        {
            return "The email is already in use, try another one";
        }
        else
        {
            authenticationService.registerUser(user);
            return "Successfully registered";
        **/
    }

}
