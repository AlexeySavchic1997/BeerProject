package by.alexeysavchic.service.interaface;

import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.SubscriptionResponse;
import by.alexeysavchic.dto.UpdateBySubscriptionDto;
import by.alexeysavchic.dto.UpdateResponseDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.dto.WarehouseXmlInfoDTO;

import java.util.List;


public interface XMLParserService {

    public List<WarehouseXmlInfoDTO> getWarehouseInfo(InputConditionDTO requestCondition);

    public List<UpdateResponseDTO> setWarehouseInfo(List<UpdateWarehouseDTO> update);

    public List<SubscriptionResponse> updateWarehouseByOrder(List<UpdateBySubscriptionDto> listUpdates);
}
