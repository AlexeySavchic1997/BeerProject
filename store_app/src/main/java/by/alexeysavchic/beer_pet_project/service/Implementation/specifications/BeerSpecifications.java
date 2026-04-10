package by.alexeysavchic.beer_pet_project.service.Implementation.specifications;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
public class BeerSpecifications {
    public Specification<Beer> getIdSpecification(Long id) {
        return (root, query, criteriaBuilder) ->
        {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"),
                    id);
        };
    }

    public Specification<Beer> getSkuSpecification(String sku) {
        return (root, query, criteriaBuilder) ->
        {
            if (sku == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("sku"),
                    sku);
        };
    }

    public Specification<Beer> getNameSpecification(String name) {
        return (root, query, criteriaBuilder) ->
        {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"),
                    "%" + name + "%");
        };
    }

    public Specification<Beer> getVolumeSpecification(BigDecimal volume) {
        return (root, query, criteriaBuilder) ->
        {
            if (volume == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("volume"),
                    volume);
        };
    }

    public Specification<Beer> getPriceSpecification(BigDecimal lowerPrice, BigDecimal upperPrice) {
        return (root, query, criteriaBuilder) ->
        {
            if (lowerPrice == null && upperPrice == null) {
                return criteriaBuilder.conjunction();
            } else if (lowerPrice == null && upperPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                        upperPrice);
            } else if (lowerPrice != null && upperPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                        lowerPrice);
            } else {
                return criteriaBuilder.between(root.get("price"),
                        lowerPrice, upperPrice);
            }
        };
    }

    public Specification<Beer> getBrandNameSpecification(String brandName) {
        return (root, query, criteriaBuilder) ->
        {
            if (brandName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<BeerBrand, Beer> brand = root.join("beerBrand");
            query.distinct(true);
            return criteriaBuilder.like(brand.get("brand_name"),
                    "%" + brandName + "%");
        };
    }

    public Specification<Beer> getCharacteristicSpecification(BeerCharacteristic type, BigDecimal lowerValue, BigDecimal upperValue) {
        return (root, query, criteriaBuilder) ->
        {
            Join<BeerCharacteristics, Beer> characteristics = root.join("beer_characteristic");
            Predicate typePredicate = criteriaBuilder.equal(characteristics.get("characteristic"), type);
            query.distinct(true);
            Predicate valuePredicate;
            if (lowerValue == null && upperValue == null) {
                return criteriaBuilder.conjunction();
            } else if (lowerValue == null && upperValue != null) {
                valuePredicate = criteriaBuilder.lessThanOrEqualTo(characteristics.get("value"), upperValue);
            } else if (lowerValue != null && upperValue == null) {
                valuePredicate = criteriaBuilder.greaterThanOrEqualTo(characteristics.get("value"), lowerValue);
            } else {
                valuePredicate = criteriaBuilder.between(characteristics.get("value"), lowerValue, upperValue);
            }
            return criteriaBuilder.and(typePredicate, valuePredicate);
        };
    }
}
