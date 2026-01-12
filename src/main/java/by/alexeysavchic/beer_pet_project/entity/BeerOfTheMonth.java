package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.id.BeerOfTheMonthId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(BeerOfTheMonthId.class)
@Entity
@Table(name = "beer_of_the_month")
public class BeerOfTheMonth
{
    @Id
    @Column(name = "month")
    @Min(value = 1)
    @Max(value = 12)
    private Short month;

    @Id
    @Column(name = "year")
    @Min(value = 2000)
    @Max(value = 2100)
    private Short year;

    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;
}


