package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.BatchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "batch")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_or_last_modified_date")
    private LocalDateTime createdOrLastModifiedDate;

    @Column(name = "count")
    @Min(value = 10, message = "count of orders must be more or equal than 10")
    @Max(value = 100, message = "count of orders must be less or equal than 100")
    private Integer count;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "status")
    private BatchStatus status;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private OrderSet orderSet;
}
