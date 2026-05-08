package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Subscription;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public Subscription findBySubscriptionType(TypeOfSubscription subscriptionType);
}
