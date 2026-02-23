package by.alexeysavchic.service.implementation;

import by.alexeysavchic.dto.XmlDto;
import by.alexeysavchic.dto.XmlDtoWrapper;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.exception.InvalidXmlFileException;
import by.alexeysavchic.exception.XmlReadingException;
import by.alexeysavchic.exception.XmlWritingException;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "by.group.project")
public class XMLParserServiceImpl implements XMLParserService
{
    private final XmlMapper mapper;

    private File xmlPath;

    private File xsdPath;

    @Override
    public XmlDtoWrapper getWarehouseInfo()
    {
        if (isValid())
        {
        try {
            XmlDtoWrapper value = mapper.readValue(xmlPath, XmlDtoWrapper.class);
            return value;
        } catch (IOException e) {
            throw new XmlReadingException();
        }
        }
        else
        {
            throw new InvalidXmlFileException();
        }
    }

    @Override
    public void setWarehouseInfo(List<UpdateWarehouseDTO> updateList)
    {
        List<XmlDto> warehouseList = getWarehouseInfo().getBeerList();
        Map<Long, XmlDto> warehouseMap=null;
        for (XmlDto dto: warehouseList)
        {
            warehouseMap.put(dto.getId(),dto);
        }
        for (UpdateWarehouseDTO dto:updateList)
        {
            XmlDto currentValue=warehouseMap.get(dto.getId());
            currentValue.setAmount(currentValue.getAmount()+dto.getAmount());
        }
        warehouseList= new ArrayList<XmlDto>(warehouseMap.values());
        try {
            mapper.writeValue(xmlPath, warehouseList);
        } catch (IOException e) {
            throw new XmlWritingException();
        }
    }

    private Validator initValidator() throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(xsdPath);
        Schema schema = factory.newSchema(schemaFile);
        return schema.newValidator();
    }

    private boolean isValid() {
        try {
            Validator validator = initValidator();
            validator.validate(new StreamSource(xmlPath));
            return true;
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            throw new XmlReadingException();
        }
    }
}
