package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSetRepository extends JpaRepository<OrderSet, Long> {
    public OrderSet findOrderSetByOrderType(OrderType orderType);
}
