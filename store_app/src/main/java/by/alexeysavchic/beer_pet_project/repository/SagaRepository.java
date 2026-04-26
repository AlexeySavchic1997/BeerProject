package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Saga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SagaRepository extends JpaRepository<Saga, UUID> {

}
