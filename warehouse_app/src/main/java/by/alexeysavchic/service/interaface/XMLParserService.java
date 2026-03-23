package by.alexeysavchic.service.interaface;

import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;

import java.util.List;


public interface XMLParserService {

    public List<WarehouseXmlInfoDTO> getWarehouseInfo(InputConditionDTO requestCondition);

    public void setWarehouseInfo(UpdateWarehouseDTO update);
}
