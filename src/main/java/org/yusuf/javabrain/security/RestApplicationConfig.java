package org.yusuf.javabrain.security;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class RestApplicationConfig extends ResourceConfig
{
    public RestApplicationConfig()
    {
        packages("org.yusuf.javabrain.security");
        //register(LoggingFilter.class);
        //register(GsonMessageBodyHandler.class);
        register(AuthenticationFilter.class);
    }


}
