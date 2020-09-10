package org.yusuf.javabrain.service;

import org.yusuf.javabrain.model.Item;
import org.yusuf.javabrain.model.Transaction;
import org.yusuf.javabrain.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.*;
import java.util.Properties;

public class TransactionService
{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public boolean buyItem(int buyerId, int itemId)
    {
        em = emf.createEntityManager();
        try
        {
            Item item = em.find(Item.class, itemId);
            User owner = em.find(User.class, item.getUserid());
            System.out.println(item + " " + item.isSold());
            if (item != null && !item.isSold())
            {
                et = em.getTransaction();
                et.begin();
                Transaction transaction = new Transaction(buyerId, item.getUserid(), itemId);
                item.setSold(true);
                em.persist(transaction);
                em.merge(item);
                et.commit();
                sendConfirmationEmail(owner.getEmail());
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            if(et != null) et.rollback();
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
        return false;
    }

    public void sendConfirmationEmail(String toEmail)
    {
        String fromEmail = "ntnutesttest";
        String password = "test123@";

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", fromEmail);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try
        {
            message.setFrom(new InternetAddress(fromEmail));
            InternetAddress toAddress = new InternetAddress(toEmail);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject("Fant website - item bought confirmation");
            message.setText("your item has been successfully sold!");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, fromEmail, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
