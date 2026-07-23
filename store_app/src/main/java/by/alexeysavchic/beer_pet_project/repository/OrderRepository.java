package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select * from orders o join order_set s on o.set_id=s.id where o.orderType=cast(:orderType as order_type) and o.status='NEW'",
            nativeQuery = true)
    public List<Order> findAllByOrderSetType(@Param("orderType") OrderType orderType);

    public List<Order> findAllByOrderTypeAndStatus(OrderType orderType, OrderStatus status);

    @Query(value = "select o from Order o join o.orderSet" +
            " where o.orderSet.id=:id")
    public List<Order> findAllByOrderSetId(@Param("id") Long id);
}
