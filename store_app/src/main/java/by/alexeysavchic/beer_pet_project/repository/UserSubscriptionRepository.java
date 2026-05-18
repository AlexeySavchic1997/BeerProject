package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    @Query(value = "select us from UserSubscription us join us.subscription" +
            " where us.timeOfExpiration>CURRENT_TIMESTAMP and us.subscription.subscriptionType='type'")
    public List<UserSubscription> findUserSubscriptionByUnexpiredDateAndSubscription(@Param("type") TypeOfSubscription type);
}
