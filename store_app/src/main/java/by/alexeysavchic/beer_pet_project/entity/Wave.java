package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Wave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private WaveStatus status;

    @Column(name = "type_of_subscription")
    @Enumerated(EnumType.STRING)
    private TypeOfSubscription typeOfSubscription;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @Column(name = "year")
    private Year year;

    @Column(name = "month")
    private Month month;

}
