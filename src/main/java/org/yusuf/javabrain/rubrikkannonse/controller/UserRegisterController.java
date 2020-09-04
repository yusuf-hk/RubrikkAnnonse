package org.yusuf.javabrain.rubrikkannonse.controller;

import org.yusuf.javabrain.rubrikkannonse.model.User;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRegisterController
{

    @PermitAll
    @POST
    public String registerUser(User user)
    {
        return "nice";
    }
}
