package org.yusuf.javabrain.security;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.service.UserService;

@Path("auth")
public class AuthenticationController
{
    private AuthenticationService authenticationService = new AuthenticationService();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user)
    {
        ResponsePojo responsePojo = new ResponsePojo();
        // Check if user email and password are presented.
        Response res = authenticationService.validateUserProps(user);
        if (res != null) return res;

        //Save user to database
        UserService userService = new UserService();
        boolean saveSuccess = userService.createUser(user);

        if (!saveSuccess)
        {
            responsePojo.setError("Email is already in use!");
            return Response.status(422).entity(responsePojo).build();
        }

        responsePojo.setToken(authenticationService.generateToken(user));

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
        Response res = authenticationService.validateUserProps(user);
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
            responsePojo.setToken(authenticationService.generateToken(existingUser));
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
        User user = authenticationService.validateToken(token);
        if (user != null)
        {
            user.setPassword(null);
            return Response.ok().entity(user).build();
        }
        else
        {
            ResponsePojo responsePojo = new ResponsePojo();
            responsePojo.setError("Invalid token.");
            return Response.ok().entity(responsePojo).build();
        }
    }
}
