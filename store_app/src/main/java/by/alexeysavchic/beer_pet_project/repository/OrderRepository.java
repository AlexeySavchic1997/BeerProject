package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
