package org.yusuf.javabrain.rubrikk.config;

import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.PasswordHash;

import org.eclipse.microprofile.auth.LoginConfig;
import org.yusuf.javabrain.security.Group;

/**
 * @author mikael
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = DatasourceProducer.JNDI_NAME,
        callerQuery = "select password from auser where userid = ?",
        groupsQuery = "select name from ausergroup where userid  = ?",
        hashAlgorithm = PasswordHash.class,
        priority = 80)
@DeclareRoles({Group.ADMIN, Group.USER})
@LoginConfig(authMethod = "MP-JWT", realmName = "template")
public class SecurityConfiguration
{
}
