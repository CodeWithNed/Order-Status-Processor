package com.sysco.dom.controller;

import com.sysco.dom.dto.response.EnterpriseResponseDTO;
import com.sysco.dom.dto.status.EnterpriseRequestDTO;
import com.sysco.dom.service.business_logic.EnterpriseResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enterprise")
@CrossOrigin
@Slf4j
public class EnterpriseController {

    private final EnterpriseResponseService enterpriseResponseService;

    @Autowired
    public EnterpriseController(EnterpriseResponseService enterpriseResponseService) {
        this.enterpriseResponseService = enterpriseResponseService;
    }

    /**
     * Process enterprise order details and return enriched response with enterprise information
     *
     * @param requestDTO Contains order details and enterprise ID
     * @return Enterprise response with order details and enterprise information
     */
    @PostMapping("/process-order")
    public ResponseEntity<EnterpriseResponseDTO> processEnterpriseOrder(@RequestBody EnterpriseRequestDTO requestDTO) {
        log.info("Received enterprise order processing request for message ID: {}", requestDTO.getMessageId());

        try {
            EnterpriseResponseDTO responseDTO = enterpriseResponseService.processEnterpriseRequest(requestDTO);
            log.info("Successfully processed enterprise order for enterprise: {}", responseDTO.getCustomerNumber());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing enterprise order: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}