package se.lexicon.g52todoapi.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.TreeSet;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "roles")
@Builder
@Entity
public class User {

    @NonNull
    @Id
    @Column(updatable = false, unique = true)
    private String email;
    @NonNull // Lombok
    @Column(nullable = false, length = 100)
    private String password;

    private boolean expired;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(@NonNull String email,@NonNull String password) {
        this.email = email;
        this.password = password;
        this.roles = new TreeSet<>();
        this.expired = false;
    }

    public void addRole(Role role){
        if (role == null) throw new IllegalArgumentException("Role cannot be null");
        if (roles == null) roles = new TreeSet<>();
        roles.add(role);
    }

    public void removeRole(Role role){
        if (role == null) throw new IllegalArgumentException("Role cannot be null");
        if (roles != null) {
            roles.remove(role);
        }else{
            //Todo: throw exceptions if needed
            throw new IllegalArgumentException(role +"is not in roles");
        }

    }




}
