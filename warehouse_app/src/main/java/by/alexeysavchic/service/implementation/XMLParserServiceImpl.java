package by.alexeysavchic.service.implementation;

import by.alexeysavchic.configuration.StreamContext;
import by.alexeysavchic.dto.GetDataFromXmlDTO;
import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.exception.InvalidXmlFileException;
import by.alexeysavchic.exception.XmlReadingException;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "by.group.project")
public class XMLParserServiceImpl implements XMLParserService {
    private final XmlMapper mapper;

    private File xmlPath;

    private File xsdPath;

    private File intermediateXml;

    private final XMLOutputFactory outputFactory;

    private final XMLInputFactory inputFactory;

    @Override
    public synchronized List<GetDataFromXmlDTO> getActualWarehouseInfo(LocalDateTime time) {
        if (isValid())
        {
            List<GetDataFromXmlDTO> listDto=new ArrayList<>();
            try(StreamContext streamContext=new StreamContext(xmlPath, intermediateXml, inputFactory, outputFactory))
            {
                XMLStreamReader xmlStreamReader = streamContext.newXmlStreamReader();
                while(xmlStreamReader.hasNext())
                {
                    xmlStreamReader.next();
                    if(xmlStreamReader.isStartElement() && xmlStreamReader.getLocalName().equals("item"))
                    {
                        GetDataFromXmlDTO dto = mapper.readValue(xmlStreamReader, GetDataFromXmlDTO.class);
                        if(dto.getLastModifiedDate().isAfter(time))
                        {
                            listDto.add(dto);
                        }
                    }
                }
            }
            catch (XMLStreamException | IOException e) {
                throw new RuntimeException(e);
            }

            return listDto;

        }
        else
        {
        throw new InvalidXmlFileException();
        }
    }

    @Override
    public synchronized List<GetDataFromXmlDTO> getFilteredWarehouseInfo(InputConditionDTO requestCondition) {
        if (isValid()) {
            List<GetDataFromXmlDTO> listDto=new ArrayList<>();
            try(StreamContext streamContext=new StreamContext(xmlPath, intermediateXml, inputFactory, outputFactory))
            {
                XMLStreamReader xmlStreamReader = streamContext.newXmlStreamReader();
                while(xmlStreamReader.hasNext()) {
                    xmlStreamReader.next();
                    if (xmlStreamReader.isStartElement() && xmlStreamReader.getLocalName().equals("item")) {
                        GetDataFromXmlDTO dto = mapper.readValue(xmlStreamReader, GetDataFromXmlDTO.class);
                        if ((requestCondition.getId() == null || dto.getId().equals(requestCondition.getId())) &&
                                (requestCondition.getZoneType() == null || dto.getZoneType().equals(requestCondition.getZoneType())) &&
                                (requestCondition.getSku() == null || dto.getSku().equals(requestCondition.getSku())) &&
                                (requestCondition.getAmount() == null || dto.getAmount().equals(requestCondition.getAmount()))) {
                            listDto.add(dto);
                        }
                    }
                }
            }
            catch (XMLStreamException | IOException e) {
                throw new RuntimeException(e);
            }
            return listDto;

        }
        else {
        throw new InvalidXmlFileException();}
    }

    @Override
    public synchronized void setWarehouseInfo(List<UpdateWarehouseDTO> updateList) {
        if (isValid())
        {
            try(StreamContext streamContext=new StreamContext(xmlPath, intermediateXml, inputFactory, outputFactory))
            {
                HashMap<String, Integer> updateMap = new HashMap<>();
                for (UpdateWarehouseDTO dto: updateList)
                {
                    updateMap.put(dto.getId().toString(),dto.getAmount());
                }
                LocalDateTime currentTime = LocalDateTime.now();
                String timeMark=currentTime.toString();
                String currentId=new String();
                boolean rewriteFlag=false;
                String currentContext=new String();
                XMLStreamReader xmlStreamReader = streamContext.newXmlStreamReader();
                XMLStreamWriter xmlStreamWriter = streamContext.newXmlStreamWriter();
                xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
                while(xmlStreamReader.hasNext())
                {
                    xmlStreamReader.next();
                    if (xmlStreamReader.isStartElement())
                    {
                        currentContext=xmlStreamReader.getLocalName();
                        xmlStreamWriter.writeStartElement(currentContext);
                    }
                    if (xmlStreamReader.isCharacters())
                    {
                        if("id".equals(currentContext) && updateMap.containsKey(xmlStreamReader.getText()))
                        {
                            currentId=xmlStreamReader.getText();
                            rewriteFlag=true;
                            xmlStreamWriter.writeCharacters(currentId);
                        }
                        else if("id".equals(currentContext) && !updateMap.containsKey(xmlStreamReader.getText()))
                        {
                            rewriteFlag=false;
                            xmlStreamWriter.writeCharacters(xmlStreamReader.getText());
                        }
                        else if("amount".equals(currentContext) && rewriteFlag)
                        {
                            Integer currentAmount=Integer.valueOf(xmlStreamReader.getText());
                            String updatedAmount=String.valueOf(currentAmount+updateMap.get(currentId));
                            xmlStreamWriter.writeCharacters(updatedAmount);
                        }
                        else if ("amount".equals(currentContext) && !rewriteFlag)
                        {
                            xmlStreamWriter.writeCharacters(xmlStreamReader.getText());
                        }
                        else if("lastModifiedDate".equals(currentContext))
                        {
                            xmlStreamWriter.writeCharacters(timeMark);
                        }
                        else
                        {
                            xmlStreamWriter.writeCharacters(xmlStreamReader.getText());
                        }
                    }
                    if (xmlStreamReader.isEndElement())
                    {
                        currentContext=null;
                        xmlStreamWriter.writeEndElement();
                    }
                }

            }
              catch (XMLStreamException | IOException e) {
                throw new RuntimeException(e);
            }
            try
            {
                Files.move(intermediateXml.toPath(), xmlPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            throw new InvalidXmlFileException();
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
