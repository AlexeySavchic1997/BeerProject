package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.response.GetSubscriptionsResponse;
import by.alexeysavchic.beer_pet_project.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SubscriptionMapper {
    List<GetSubscriptionsResponse> listSubscriptionToListGetSubscriptionsResponse(List<Subscription> subscription);

    GetSubscriptionsResponse subscriptionToGetSubscriptionsResponse(Subscription subscription);
}
