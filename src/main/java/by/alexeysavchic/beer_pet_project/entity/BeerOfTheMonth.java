package by.alexeysavchic.beer_pet_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "beer_of_the_month")
public class BeerOfTheMonth
{

    @Id
    @Column(name = "month")
    Byte month;
    @Id
    @Column(name = "month")
    Integer year;

    Beer beer;
}
