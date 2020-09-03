package org.yusuf.javabrain.security;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class AuthRunOnStartup
{
    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void init()
    {
        long groups = (long) em.createQuery("SELECT count(g.name) FROM Group g").getSingleResult();
        if(groups == 0)
        {
            em.persist(new Group(Group.USER));
            em.persist(new Group(Group.ADMIN));
        }
    }
}
