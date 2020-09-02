package org.yusuf.javabrain.service;

import org.yusuf.javabrain.model.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class ItemService
{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public List<Item> getAllitems()
    {
        em = emf.createEntityManager();
        List<Item> list = em.createQuery("FROM Item", Item.class).getResultList();
        return new ArrayList<>(list);
    }

    public Item getItem(int id)
    {
        em = emf.createEntityManager();
        String query = "SELECT m FROM Item m WHERE m.id = :id";
        TypedQuery<Item> tq = em.createQuery(query, Item.class);
        tq.setParameter("id", id);
        Item item = null;
        try
        {
            item = tq.getSingleResult();
            return item;
        } catch (NoResultException ex)
        {
            System.out.println(ex);
        } finally
        {
            em.close();
        }
        return item;
    }

    public Item addItem(Item item)
    {

        em = emf.createEntityManager();
        String query = "SELECT max (id) from Item";
        TypedQuery<Integer> tq = em.createQuery(query, Integer.class);
        et = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            item.setId(tq.getSingleResult() + 1);
            em.persist(item);
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
        return item;
    }

    public Item updateItem(Item item)
    {
        em = emf.createEntityManager();
        Item newItem = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            newItem = em.find(Item.class, item.getId());
            newItem.setItemName(item.getItemName());
            newItem.setDescription(item.getDescription());
            newItem.setPrice(item.getPrice());
            em.persist(newItem);
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
        return newItem;
    }

    public Item removeItem(int id)
    {
        em = emf.createEntityManager();
        Item item = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            item = em.find(Item.class, id);
            em.remove(item);
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
        return item;
    }
}