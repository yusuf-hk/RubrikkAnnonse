package org.yusuf.javabrain.model;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
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

    private boolean sold;

    @PrePersist
    public void onCreate()
    {
        created = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        sold = false;
    }
}