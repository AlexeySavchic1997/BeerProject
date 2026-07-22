package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSetRepository extends JpaRepository<OrderSet, Long>, JpaSpecificationExecutor<OrderSet> {
    public List<OrderSet> findAllByIdIn(List<Long> ids);

    public OrderSet findTopByOrderSetStatus(OrderSetStatus orderSetStatus);
}
