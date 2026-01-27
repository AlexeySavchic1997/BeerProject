package by.alexeysavchic.beer_pet_project.security;

import by.alexeysavchic.beer_pet_project.entity.User;

public interface SecurityContextService
{
    public User getCurrentUser();
}
