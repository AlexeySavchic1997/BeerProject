package by.alexeysavchic.beer_pet_project.service.Implementation.specifications;

import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class BeerBrandSpecifications {
    public Specification<BeerBrand> getNameSpecification(String name) {
        return (root, query, criteriaBuilder) ->
        {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("brand_name"),
                    "%" + name + "%");
        };
    }
}
