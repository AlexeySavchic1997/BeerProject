package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Wave
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "status")
    WaveStatus status;

    @Column(name = "processed_date")
    LocalDateTime processedDate;

    @Column(name = "year")
    Year year;

    @Column(name = "month")
    Month month;
}
