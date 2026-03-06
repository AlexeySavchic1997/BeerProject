package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.security.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @Size(min = 2, max = 20, message = "username must be between 2 and 20 symbols")
    @NotBlank
    private String username;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "email")
    @Email
    @NotBlank
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
