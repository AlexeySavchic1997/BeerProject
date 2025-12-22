package by.alexeysavchic.beer_pet_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;
import java.util.HashMap;
@Entity
@Table(name = "beer")
@Getter
@Setter
@NoArgsConstructor
public class Beer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "volume")
    Float volume;
    @Column(name = "price")
    Integer price;
    @ManyToOne
    @JoinColumn(name="brand_id")
    BeerBrand beerBrand;

    EnumMap<Beer_Characteristic, Float> characteristics;
}
