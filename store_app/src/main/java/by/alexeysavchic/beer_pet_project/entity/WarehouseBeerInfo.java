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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "warehouse_beer_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"sku", "beer_id"})})
public class WarehouseBeerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sku")
    @Size(max = 30, message = "unvalid sku")
    @NotBlank
    private String sku;

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
