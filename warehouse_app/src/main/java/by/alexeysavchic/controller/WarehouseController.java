package by.alexeysavchic.controller;

import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.service.interaface.XMLParserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class WarehouseController {
    private XMLParserService parserService;

    @PostMapping("/update")
    public void updateWarehouseInfo(@RequestBody List<UpdateWarehouseDTO> request) {
        parserService.setWarehouseInfo(request);
    }
}
