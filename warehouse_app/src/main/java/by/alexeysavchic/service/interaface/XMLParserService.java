package by.alexeysavchic.service.interaface;

import by.alexeysavchic.dto.XmlDtoWrapper;
import by.alexeysavchic.dto.UpdateWarehouseDTO;

import java.util.List;


public interface XMLParserService
{
    public XmlDtoWrapper getWarehouseInfo();

    public void setWarehouseInfo(List<UpdateWarehouseDTO> updateList);
}
