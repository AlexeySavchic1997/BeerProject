package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.SplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;

import java.util.List;

public interface OrderSetService {
    public List<GetOrderSetResponse> findAll();

    public List<GetOrderSetResponse> findAllByOrderType(OrderType orderType);

    public void markSplit(SplitRequest request);

    public void split();

}
