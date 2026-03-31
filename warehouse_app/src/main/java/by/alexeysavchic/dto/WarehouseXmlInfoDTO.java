package by.alexeysavchic.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "item")
public class WarehouseXmlInfoDTO {
    @JacksonXmlProperty(localName = "id")
    @PositiveOrZero(message = "Id can't be negative")
    private Long id;

    @JacksonXmlProperty(localName = "zoneType")
    @NotBlank
    private ZoneType zoneType;

    @JacksonXmlProperty(localName = "sku")
    @NotBlank
    private String sku;

    @JacksonXmlProperty(localName = "amount")
    @PositiveOrZero(message = "Amount of items can't be negative")
    private Integer amount;

    @JacksonXmlProperty(localName = "lastModifiedDate")
    @PastOrPresent(message = "Date of modifications can't be in future")
    private LocalDateTime lastModifiedDate;
}
