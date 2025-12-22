package by.alexeysavchic.beer_pet_project.entity;

import java.time.LocalDateTime;

public class UserSubscription
{
    Integer id;
    LocalDateTime timeOfExpiration;
    User user;
    Beer beer;
    Subscription subscription;
}
