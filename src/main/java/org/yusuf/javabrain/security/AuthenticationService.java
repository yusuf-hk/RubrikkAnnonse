package org.yusuf.javabrain.security;

import org.mindrot.jbcrypt.BCrypt;
import org.yusuf.javabrain.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class AuthenticationService
{
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JEETut3");
    private EntityManager em;
    private EntityTransaction et;

    public void registerUser(User user)
    {
        String query = "SELECT max(id) from User";
        TypedQuery<Integer> tq = em.createQuery(query, Integer.class);
        et = null;
        try
        {
            user.setEmail(generateHash(user.getEmail()));
            user.setPassword(generateHash(user.getPassword()));
            et = em.getTransaction();
            et.begin();
            user.setId(tq.getSingleResult() + 1);
            em.persist(user);
            et.commit();
        } catch (Exception e)
        {
            if (et != null)
            {
                et.rollback();
            }
            e.printStackTrace();
        } finally
        {
            em.close();
        }
    }

    public boolean checkEmailExist(String email)
    {
        String hashEmail = generateHash(email);
        em = emf.createEntityManager();
        Query query = em.createQuery("SELECT count(e) FROM User e WHERE e.email =:hashEmail", Long.class);
        query.setParameter("hashEmail", hashEmail);
        long result = (long) query.getSingleResult();
        System.out.println((result >= 1) + " pass");
        return result >= 1;
    }

    public boolean checkPasswordExist(String password)
    {
        String hashPassword = generateHash(password);
        em = emf.createEntityManager();
        Query query = em.createQuery("SELECT count(e) FROM User e WHERE e.password =:hashPassword", Long.class);
        query.setParameter("hashPassword", hashPassword);
        long result = (long) query.getSingleResult();
        System.out.println((result >= 1) + " pass");
        return result >= 1;
    }

    /** this doesnt work, its maybe wrong. will check it out later
    private String getEmail(String email)
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
     **/

    // <---------------------- All of these is to hash the password ---------------------->

    public String generateHash(String hash)
    {
        String hashtext = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(hash.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashtext = no.toString(16);
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashtext;
    }

    /** I dont know what is going on. this is third time trying
    public String generateHash(String hash)
    {
        return BCrypt.hashpw(hash, BCrypt.gensalt(12));
    }
     **/

    /** Old hash system, didnt not worked out like i thought it was to be. May look at it later
    public String generateHash(String password)
    {
        String result = "";
        try
        {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array)
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
     **/
}
