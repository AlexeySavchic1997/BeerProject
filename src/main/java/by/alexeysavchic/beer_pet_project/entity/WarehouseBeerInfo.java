package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "warehouse_beer_info")
public class WarehouseBeerInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "zone")
    @Enumerated(EnumType.STRING)
    private ZoneType zoneType;

    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Column(name = "amount")
    @Positive
    private Integer amount;
}
