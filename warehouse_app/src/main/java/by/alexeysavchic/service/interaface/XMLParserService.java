package by.alexeysavchic.service.interaface;

import by.alexeysavchic.dto.GetDataFromXmlDTO;
import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;

import java.time.LocalDateTime;
import java.util.List;


public interface XMLParserService {
    public List<GetDataFromXmlDTO> getActualWarehouseInfo(LocalDateTime time);

    public List<GetDataFromXmlDTO> getFilteredWarehouseInfo(InputConditionDTO requestCondition);

    public void setWarehouseInfo(List<UpdateWarehouseDTO> updateList);
}
