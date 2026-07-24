package by.alexeysavchic.service;

import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.WarehouseInfoXmlDTOWrapper;
import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import by.alexeysavchic.exception.NotValidXmlItemException;
import by.alexeysavchic.exception.XmlReadingException;
import by.alexeysavchic.service.implementation.XMLParserServiceImpl;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class XMLParserServiceTest {

    @Mock
    private XmlMapper mapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private XMLParserServiceImpl xmlParserService;

    private File dummyFile;

    @BeforeEach
    void setUp() {
        dummyFile = new File("dummy.xml");
        xmlParserService.setXmlPath(dummyFile);
    }

    @Nested
    class getWarehouseInfoTests {

        @Test
        void successfulRetrievalWithFilteringBySku() throws IOException {
            InputConditionDTO condition = new InputConditionDTO();
            condition.setSku("BEER-1");

            WarehouseXmlInfoDTO matchingItem = new WarehouseXmlInfoDTO();
            matchingItem.setSku("BEER-1");

            WarehouseXmlInfoDTO nonMatchingItem = new WarehouseXmlInfoDTO();
            nonMatchingItem.setSku("BEER-2");

            WarehouseInfoXmlDTOWrapper wrapper = new WarehouseInfoXmlDTOWrapper();
            wrapper.setWarehouseXmlInfoDTOS(new ArrayList<>(List.of(matchingItem, nonMatchingItem)));

            when(mapper.readValue(dummyFile, WarehouseInfoXmlDTOWrapper.class)).thenReturn(wrapper);
            when(validator.validate(any(WarehouseXmlInfoDTO.class))).thenReturn(Collections.emptySet());

            List<WarehouseXmlInfoDTO> result = xmlParserService.getWarehouseInfo(condition);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("BEER-1", result.get(0).getSku());

            verify(mapper, times(1)).readValue(dummyFile, WarehouseInfoXmlDTOWrapper.class);
            verify(validator, times(2)).validate(any(WarehouseXmlInfoDTO.class));
        }

        @Test
        void successfulRetrievalWithFilteringByDate() throws IOException {
            LocalDateTime conditionDate = LocalDateTime.of(2026, 1, 1, 12, 0);
            InputConditionDTO condition = new InputConditionDTO();
            condition.setLastModifiedDate(conditionDate);

            WarehouseXmlInfoDTO newVat = new WarehouseXmlInfoDTO();
            newVat.setLastModifiedDate(conditionDate.plusDays(1));

            WarehouseXmlInfoDTO oldVat = new WarehouseXmlInfoDTO();
            oldVat.setLastModifiedDate(conditionDate.minusDays(1));

            WarehouseInfoXmlDTOWrapper wrapper = new WarehouseInfoXmlDTOWrapper();
            wrapper.setWarehouseXmlInfoDTOS(new ArrayList<>(List.of(newVat, oldVat)));

            when(mapper.readValue(dummyFile, WarehouseInfoXmlDTOWrapper.class)).thenReturn(wrapper);
            when(validator.validate(any())).thenReturn(Collections.emptySet());

            List<WarehouseXmlInfoDTO> result = xmlParserService.getWarehouseInfo(condition);

            assertEquals(1, result.size());
            assertEquals(conditionDate.plusDays(1), result.get(0).getLastModifiedDate());
        }

        @Test
        void throwsNotValidXmlItemExceptionWhenValidationFails() throws IOException {
            InputConditionDTO condition = new InputConditionDTO();

            WarehouseXmlInfoDTO invalidItem = new WarehouseXmlInfoDTO();
            WarehouseInfoXmlDTOWrapper wrapper = new WarehouseInfoXmlDTOWrapper();
            wrapper.setWarehouseXmlInfoDTOS(new ArrayList<>(List.of(invalidItem)));

            when(mapper.readValue(dummyFile, WarehouseInfoXmlDTOWrapper.class)).thenReturn(wrapper);

            ConstraintViolation<WarehouseXmlInfoDTO> violation = mock(ConstraintViolation.class);
            when(violation.getMessage()).thenReturn("Amount of items can't be negative");
            when(validator.validate(invalidItem)).thenReturn(Set.of(violation));

            NotValidXmlItemException exception = assertThrows(NotValidXmlItemException.class,
                    () -> xmlParserService.getWarehouseInfo(condition));

            assertEquals("warehouse service exception: Amount of items can't be negative", exception.getMessage());
        }

        @Test
        void throwsXmlReadingExceptionWhenIoExceptionOccurs() throws IOException {
            InputConditionDTO condition = new InputConditionDTO();

            when(mapper.readValue(dummyFile, WarehouseInfoXmlDTOWrapper.class)).thenThrow(new IOException("File not found"));

            XmlReadingException exception = assertThrows(XmlReadingException.class,
                    () -> xmlParserService.getWarehouseInfo(condition));

            assertNotNull(exception.getCause());
            assertEquals(IOException.class, exception.getCause().getClass());
        }
    }
}
