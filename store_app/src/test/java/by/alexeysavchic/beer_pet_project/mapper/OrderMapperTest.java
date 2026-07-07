package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.response.GetOrderItemResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderMapperTest {

    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void shouldMapOrderItemToOrderItemResponseWithBeerName() {
        Beer beer = new Beer();
        beer.setName("Guinness");

        OrderItem orderItem = new OrderItem();
        orderItem.setBeer(beer);
        orderItem.setQuantity(5);
        orderItem.setPrice(new BigDecimal("10.50"));

        GetOrderItemResponse response = mapper.orderItemToOrderItemResponse(orderItem);

        assertNotNull(response);
        assertEquals("Guinness", response.getBeerName());
        assertEquals(5, response.getQuantity());
        assertEquals(new BigDecimal("10.50"), response.getPrice());
    }
}
