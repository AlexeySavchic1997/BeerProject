package by.alexeysavchic.beer_pet_project.service.Implementation.specifications;

import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.SplitType;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrderSetSpecifications {
    public Specification<OrderSet> getStatusSpecification(OrderSetStatus status) {
        return (root, query, criteriaBuilder) ->
        {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"),
                    status);
        };
    }

    public Specification<OrderSet> getTagSpecification(SplitType splitType) {
        return (root, query, criteriaBuilder) ->
        {
            if (splitType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("split_type"),
                    splitType);
        };
    }

    public Specification<OrderSet> getOrderTypeSpecification(OrderType type) {
        return (root, query, criteriaBuilder) ->
        {
            if (type == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type"),
                    type);
        };
    }
}
