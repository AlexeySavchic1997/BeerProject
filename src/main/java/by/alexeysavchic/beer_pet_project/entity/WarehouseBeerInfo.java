package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.IDs.WarehouseBeerInfoId;
import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "warehouse_beer_info")
@IdClass(WarehouseBeerInfoId.class)
public class WarehouseBeerInfo
{
@Id
@Column(name = "zone")
@Enumerated(EnumType.STRING)
ZoneType zoneType;

@Id
@ManyToOne
@JoinColumn(name = "beer_id")
Beer beer;

@Column(name = "amount")
@Positive
Integer amount;
}
