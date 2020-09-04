package org.yusuf.javabrain.rubrikkannonse.service;

import org.yusuf.javabrain.rubrikkannonse.model.Message;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService
{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public List<Message> getAllMessages()
    {
        em = emf.createEntityManager();
        List<Message> list = em.createQuery("FROM Message", Message.class).getResultList();
        return new ArrayList<>(list);
    }

    public Message getMessage(long id)
    {
        em = emf.createEntityManager();
        String query = "SELECT m FROM Message m WHERE m.id = :id";
        TypedQuery<Message> tq = em.createQuery(query, Message.class);
        tq.setParameter("id", id);
        Message message = null;
        try
        {
            message = tq.getSingleResult();
            return message;
        } catch (NoResultException ex)
        {
            System.out.println(ex);
        } finally
        {
            em.close();
        }
        return message;
    }

    public Message addMessage(Message message)
    {

        em = emf.createEntityManager();
        String query = "SELECT max (id) from Message";
        TypedQuery<Integer> tq = em.createQuery(query, Integer.class);
        et = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            message.setId(tq.getSingleResult() + 1);
            em.persist(message);
            et.commit();
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
        return message;
    }

    public Message updateMessage(Message message)
    {
        em = emf.createEntityManager();
        Message newMessage = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            newMessage = em.find(Message.class, message.getId());
            newMessage.setMessage(message.getMessage());
            em.persist(newMessage);
            et.commit();
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
        return newMessage;
    }

    public Message removeMessage(long id)
    {
        em = emf.createEntityManager();
        Message message = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            message = em.find(Message.class, id);
            em.remove(message);
            et.commit();
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
        return message;
    }
}
