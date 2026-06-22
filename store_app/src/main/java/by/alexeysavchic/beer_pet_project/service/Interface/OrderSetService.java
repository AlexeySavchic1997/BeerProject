package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;

import java.util.List;

public interface OrderSetService {
    public List<GetOrderSetResponse> getOrderSets(GetOrderSetsRequest request);

    public void markSplit(OrderSetSplitRequest request);

    public void split();

}
