package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerCharacteristicsRepository extends JpaRepository<BeerCharacteristics, Long> {
}
