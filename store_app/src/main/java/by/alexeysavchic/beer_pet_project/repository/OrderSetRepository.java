package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSetRepository extends JpaRepository<OrderSet, Long> {

    @Query(value = "select os from OrderSet os join fetch os.orders where os.orderType = 'type'")
    public List<OrderSet> findOrderSetByOrderType(@Param("type") OrderType orderType);

    public OrderSet findTopByOrderSetStatus(OrderSetStatus orderSetStatus);
}
