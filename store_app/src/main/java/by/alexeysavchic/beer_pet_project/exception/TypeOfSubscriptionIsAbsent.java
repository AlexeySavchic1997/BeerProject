package by.alexeysavchic.beer_pet_project.exception;

import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;

public class TypeOfSubscriptionIsAbsent extends RuntimeException {
    public TypeOfSubscriptionIsAbsent(TypeOfSubscription type) {
        super(ErrorMessages.typeOfSubscriptionNotFound);
    }
}
