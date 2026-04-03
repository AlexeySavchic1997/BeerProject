package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeerBrandRepository extends JpaRepository<BeerBrand, Long>
{
    public Optional<BeerBrand> findByBrandName(String brandName);
}
