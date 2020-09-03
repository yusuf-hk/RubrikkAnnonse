package org.yusuf.javabrain.rubrikk.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;


/**
 * @author mikael
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends ResourceConfig
{
    public JAXRSConfiguration()
    {
        packages(true, "org.yusuf.javabrain.rubrikk", "org.yusuf.javabrain.security")
                // Now you can expect validation errors to be sent to the client.
                .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
                // @ValidateOnExecution annotations on subclasses won't cause errors.
                .property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true)
                .register(MultiPartFeature.class);
    }
}