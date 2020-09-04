package org.yusuf.javabrain.security;

import javax.ejb.Stateless;
import javax.inject.Inject;;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.xml.ws.Response;

@Stateless
@Path("auth")
public class AuthController
{
    @Inject
    private Auth0AuthenticationConfig authenticationConfig;

    @POST
    public Response authenticate(JsonObject loginDetails)
    {
        String username = loginDetails.getString("username");
        String password = loginDetails.getString("password");

        try
        {
            JSONOB
        }
        catch (UnirestEx)
    }
}
