package org.yusuf.javabrain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement
@Entity
@Table(name = "\"User\"")
public class User implements Serializable
{
    @Id
    private Integer id;

    @NotNull(message = "Firstname cannot be null")
    private String firstName;

    @NotNull(message = "Lastname cannot be null")
    private String lastName;

    @NotNull @Email(message = "Email should be valid")
    private String email;

    private String password;

    private String created;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userid")
    private List<Item> userItems;

    @PrePersist
    private void onCreate()
    {
        created = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }
}
