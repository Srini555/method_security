package com.amisoftdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "authorities")
@Data
public class User {

    public User(String email, String password, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities.addAll(authorities);
    }

    public User(String email, String password) {
        this( email,password,new HashSet<>());
    }


    public User(String email, String password, Authority... authorities) {

        this( email,password,new HashSet<>(Arrays.asList(authorities)));
    }

    @Id
    @GeneratedValue
    private Long id;

    private String email, password;


    @ManyToMany(mappedBy = "users")
    private List<Authority> authorities = new ArrayList<>();


}
