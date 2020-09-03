package org.yusuf.javabrain.rubrikk.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "message")
@XmlRootElement
public class Message implements Serializable
{
    @Id
    private int id;

    private String message;

    private String created;

    private String author;

    public Message()
    {

    }

    public Message(String message, String author)
    {
        this.message = message;
        this.author = author;
        this.created = new SimpleDateFormat("yyyy.MM.dd.HH").format(new Date());
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @PrePersist
    public void onCreate()
    {
        created = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getCreated()
    {
        return created;
    }

    public void setCreated(String created)
    {
        this.created = created;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}
