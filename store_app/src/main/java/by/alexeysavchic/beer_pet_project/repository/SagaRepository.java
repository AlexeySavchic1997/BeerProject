package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Saga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SagaRepository extends JpaRepository<Saga, UUID>
{
    @Query(value = "select * from transaction_outbox " +
            "where status='STARTED' and updated_at<:delay",
    nativeQuery = true)
    public List<Saga> findAllNotCompletedSagas(@Param("delay") LocalDateTime delay);

}
