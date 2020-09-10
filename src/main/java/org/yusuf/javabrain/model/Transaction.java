package org.yusuf.javabrain.model;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@Entity
@Table(name = "transaction")
public class Transaction
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer buyerId;

    private Integer sellerId;

    private Integer itemId;

    public Transaction(Integer buyerId, Integer sellerId, Integer itemId)
    {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
    }

    public Transaction()
    {

    }
}
