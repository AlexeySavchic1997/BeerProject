package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.SagaStage;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction_outbox")
public class Saga {
    @Id
    @Column(name = "id")
    UUID id;

    @Column(name = "status")
    SagaStatus status;

    @Column(name = "stage")
    SagaStage stage;

    @Column(name = "payload")
    @JdbcTypeCode(value = SqlTypes.JSON)
    JsonNode payload;

    @Column(name = "created_at")
    @PastOrPresent
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @PastOrPresent
    LocalDateTime updatedAt;
}
