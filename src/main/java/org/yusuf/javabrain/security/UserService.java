package org.yusuf.javabrain.security;

import org.yusuf.javabrain.model.Item;
import org.yusuf.javabrain.security.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class UserService
{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public void registerUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        user.setEmail(generateHash(user.getEmail()));
        user.setPassword(generateHash(user.getPassword()));
        String query = "SELECT max(id) from User";
        TypedQuery<Integer> tq = em.createQuery(query, Integer.class);
        et = null;
        try
        {
            et = em.getTransaction();
            et.begin();
            user.setId(tq.getSingleResult() + 1);
            em.persist(user);
            et.commit();
        }
        catch (Exception e)
        {
            if (et != null)
            {
                et.rollback();
            }
            e.printStackTrace();
        }
        finally
        {
            em.close();
        }
    }

    public boolean checkEmail(String email) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        String hashEmail = generateHash(email);
        return getEmail(email).equals(hashEmail) && getEmail(email) != null;
    }

    public boolean checkPassword (String password) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        String hashPassword = generateHash(password);
        return getPassword(password).equals(hashPassword) && getPassword(password) != null;
    }

    public String getEmail(String email)
    {
        em = emf.createEntityManager();
        String query = "SELECT e FROM User e WHERE e.email = :email";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("email", email);
        User user = null;
        try
        {
            user = tq.getSingleResult();
            return user.getEmail();
        } catch (NoResultException e)
        {
            System.out.println(e);
        } finally
        {
            em.close();
        }
        return user.getEmail();
    }

    public String getPassword(String password)
    {
        em = emf.createEntityManager();
        String query = "SELECT e FROM User e WHERE e.password = :password";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("password", password);
        User user = null;
        try
        {
            user = tq.getSingleResult();
            return user.getPassword();
        } catch (NoResultException e)
        {
            System.out.println(e);
        } finally
        {
            em.close();
        }
        return user.getPassword();
    }

    // <---------------------- All of these is to hash the password ---------------------->
    private String generateHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
        {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else
        {
            return hex;
        }
    }
}
