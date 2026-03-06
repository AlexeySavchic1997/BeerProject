package by.alexeysavchic.dto;

import by.alexeysavchic.clases.ZoneType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.type.DateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "item")
public class GetDataFromXmlDTO
{
    @JacksonXmlProperty(localName = "id")
    private Long id;

    @JacksonXmlProperty(localName = "zoneType")
    private ZoneType zoneType;

    @JacksonXmlProperty(localName = "sku")
    private String sku;

    @JacksonXmlProperty(localName = "amount")
    private Integer amount;

    @JacksonXmlProperty(localName = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;
}
