package by.alexeysavchic.service.implementation;

import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.dto.WarehouseInfoXmlDTOWrapper;
import by.alexeysavchic.exception.NotValidXmlItemException;
import by.alexeysavchic.exception.XmlReadingException;
import by.alexeysavchic.exception.XmlWritingException;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "by.group.project")
public class XMLParserServiceImpl implements XMLParserService {
    private final XmlMapper mapper;

    private File xmlPath;

    private final Validator validator;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public List<WarehouseXmlInfoDTO> getWarehouseInfo(@Valid InputConditionDTO requestCondition) {
        try {
            lock.readLock().lock();
            WarehouseInfoXmlDTOWrapper warehouseInfo = mapper.readValue(xmlPath, WarehouseInfoXmlDTOWrapper.class);
            List<WarehouseXmlInfoDTO> warehouseXmlInfoDTOS = warehouseInfo.getWarehouseXmlInfoDTOS();
            Iterator<WarehouseXmlInfoDTO> iterator = warehouseXmlInfoDTOS.iterator();
            while (iterator.hasNext()) {
                WarehouseXmlInfoDTO item = iterator.next();
                Set<ConstraintViolation<WarehouseXmlInfoDTO>> unvalidFields = validator.validate(item);
                if (!unvalidFields.isEmpty()) {
                    for (ConstraintViolation<WarehouseXmlInfoDTO> unvalidField : unvalidFields) {
                        throw new NotValidXmlItemException(unvalidField.getMessage());
                    }
                } else if (!((requestCondition.getLastModifiedDate() == null || item.getLastModifiedDate().isAfter(requestCondition.getLastModifiedDate())) &&
                        (requestCondition.getId() == null || item.getId().equals(requestCondition.getId())) &&
                        (requestCondition.getZoneType() == null || item.getZoneType().equals(requestCondition.getZoneType())) &&
                        (requestCondition.getSku() == null || item.getSku().equals(requestCondition.getSku())) &&
                        (requestCondition.getAmount() == null || item.getAmount().equals(requestCondition.getAmount())))) {
                    iterator.remove();
                }
            }
            return warehouseXmlInfoDTOS;
        } catch (IOException e) {
            throw new XmlReadingException(xmlPath.toString(), e);
        } finally {
            lock.readLock().unlock();
        }
    }


    private List<WarehouseXmlInfoDTO> getWarehouseInfo() {
        try {
            WarehouseInfoXmlDTOWrapper warehouseInfo = mapper.readValue(xmlPath, WarehouseInfoXmlDTOWrapper.class);
            List<WarehouseXmlInfoDTO> warehouseXmlInfoDTOS = warehouseInfo.getWarehouseXmlInfoDTOS();
            return warehouseXmlInfoDTOS;
        } catch (IOException e) {
            throw new XmlReadingException(xmlPath.toString(), e);
        }
    }

    @Override
    public void setWarehouseInfo(@Valid UpdateWarehouseDTO update) {
        try {
            lock.writeLock().lock();
            List<WarehouseXmlInfoDTO> warehouseList = getWarehouseInfo();
            for (WarehouseXmlInfoDTO dto : warehouseList) {
                if (dto.getId().equals(update.getId())) {
                    dto.setAmount(update.getAmount());
                    dto.setLastModifiedDate(LocalDateTime.now());
                    break;
                }
            }
            WarehouseInfoXmlDTOWrapper wrapper = new WarehouseInfoXmlDTOWrapper();
            wrapper.setWarehouseXmlInfoDTOS(warehouseList);
            mapper.writeValue(xmlPath, wrapper);
        } catch (IOException e) {
            throw new XmlWritingException(xmlPath.toString(), e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
