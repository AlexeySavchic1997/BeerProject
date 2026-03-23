package by.alexeysavchic.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "items")
public class WarehouseInfoXmlDTOWrapper {
    List<WarehouseXmlInfoDTO> warehouseXmlInfoDTOS;
}
