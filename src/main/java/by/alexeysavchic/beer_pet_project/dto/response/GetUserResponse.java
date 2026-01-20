package by.alexeysavchic.beer_pet_project.dto.response;

import by.alexeysavchic.beer_pet_project.security.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GetUserResponse
{
    private long id;

    private String username;

    private String email;

    private Set<Role> roles;
}
