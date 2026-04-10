package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeerBrandRepository extends JpaRepository<BeerBrand, Long>, JpaSpecificationExecutor<BeerBrand> {
    public boolean existsByBrandName(String name);

    public Optional<BeerBrand> findByBrandName(String brandName);
}
