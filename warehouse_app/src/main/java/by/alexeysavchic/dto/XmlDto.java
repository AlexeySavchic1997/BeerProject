package by.alexeysavchic.dto;

import by.alexeysavchic.clases.ZoneType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class XmlDto
{
    private Long id;

    private ZoneType zoneType;

    private Long SKU;

    private Integer amount;
}
