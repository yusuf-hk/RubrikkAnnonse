package org.yusuf.javabrain.service;

import org.apache.log4j.Logger;
import org.yusuf.javabrain.model.User;
import org.yusuf.javabrain.security.CypherUtils;
import org.hibernate.Session;

import javax.persistence.*;

public class UserService
{
    private final Logger LOGGER = Logger.getLogger(UserService.class);

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public boolean createUser(User user)
    {
        boolean found = false;
        em = emf.createEntityManager();
        String query = "SELECT max (id) from User";
        TypedQuery<Integer> tq = em.createQuery(query, Integer.class);
        et = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            user.setId(tq.getSingleResult() + 1);
            em.persist(user);
            et.commit();
            found = true;
        } catch (Exception ex)
        {
            if (et != null)
            {
                et.rollback();
            }
            ex.printStackTrace();
        } finally
        {
            em.close();
        }
        return found;
    }

    public User findUserByEmail(String email)
    {
        em = emf.createEntityManager();
        String query = "SELECT m FROM User m WHERE m.email = :email";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("email", email);
        User user = null;
        try
        {
            user = tq.getSingleResult();
            return user;
        }
        catch (NoResultException ex)
        {
            System.out.println(ex);
        } finally
        {
            em.close();
        }
        return user;
    }

    public User findUserById(Long id)
    {
        em = emf.createEntityManager();
        String query = "SELECT m FROM User m WHERE m.id = :id";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("id", id);
        User user = null;
        try
        {
            user = tq.getSingleResult();
            return user;
        } catch (NoResultException ex)
        {
            System.out.println(ex);
        } finally
        {
            em.close();
        }
        return user;
    }
}
