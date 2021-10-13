package it.bx.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormUser {
    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;
    String name;
    String username;
    String password;
    @ManyToMany(fetch = EAGER)
    Collection<FormRole> roles = new ArrayList<>();
}
