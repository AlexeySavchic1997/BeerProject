package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.security.Role;

import java.util.List;

public class User
{
    Integer id;
    String username;
    String password;
    String email;
    List<Role> roles;
    UserSubscription subscription;
    List<Order> orders;
}
