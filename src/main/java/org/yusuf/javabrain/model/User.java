package org.yusuf.javabrain.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "User")
public class User implements Serializable
{

    @Id
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String created;

    @PrePersist
    private void onCreate()
    {
        created = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }

}
