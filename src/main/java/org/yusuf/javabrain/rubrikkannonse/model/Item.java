package org.yusuf.javabrain.rubrikkannonse.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "item")
@XmlRootElement
public class Item implements Serializable
{
    @Id
    private int id;

    private String itemName;

    private String created;

    private int price;

    private String description;

    public Item()
    {

    }

    public Item(String itemName, int price, String description)
    {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
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

    public String getCreated()
    {
        return created;
    }

    public void setCreated(String created)
    {
        this.created = created;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}