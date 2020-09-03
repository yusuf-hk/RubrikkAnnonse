package org.yusuf.javabrain.security;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Table(name = "AGROUP")
@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(exclude = "users")
public class Group
{
    public static final String USER = "users";
    public static final String ADMIN = "admin";
    public static final String[] GROUPS = {USER, ADMIN};

    @Id
    String name;

    String project;

    @JsonbTransient
    @ManyToMany
    @JoinTable(name="AUSERGROUP",
            joinColumns = @JoinColumn(name="name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name="userid",referencedColumnName = "userid"))
    List<User> users;

    public Group(String user)
    {
    }
}
