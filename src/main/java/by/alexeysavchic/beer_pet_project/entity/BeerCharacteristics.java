package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.id.BeerCharacteristicsId;
import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@IdClass(BeerCharacteristicsId.class)
@Entity
@Table(name = "beer_characteristic")
public class BeerCharacteristics
{
    @Id
    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Id
    @Column(name = "characteristic")
    @Enumerated(EnumType.STRING)
    private BeerCharacteristic characteristic;

    @Column(name = "value")
    @Positive
    private BigDecimal value;
}
