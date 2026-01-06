package by.alexeysavchic.beer_pet_project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "beer")
public class Beer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @Size(min = 2, max = 20, message = "beer name must be between 2 and 20 symbols")
    @NotBlank
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "volume")
    @Positive
    private BigDecimal volume;

    @Column(name = "price")
    @Positive
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private BeerBrand beerBrand;

    @OneToMany(mappedBy = "beer", cascade = CascadeType.REMOVE)
    private List<BeerCharacteristics> characteristics;

    @ManyToMany(mappedBy = "beers")
    private Set<UserSubscription> userSubscriptions;
}
