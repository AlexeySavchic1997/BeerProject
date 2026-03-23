package by.alexeysavchic.controller;

import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.service.interaface.XMLParserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class WarehouseController {
    private XMLParserService parserService;

    @PostMapping("/update")
    public ResponseEntity<String> updateWarehouse(@RequestBody @Valid UpdateWarehouseDTO request,
                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String identifier) {
        if (identifier.equals("worker")) {
            parserService.setWarehouseInfo(request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


}
