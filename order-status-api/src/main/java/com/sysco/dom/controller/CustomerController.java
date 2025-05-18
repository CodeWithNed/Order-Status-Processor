package com.sysco.dom.controller;

import com.sysco.dom.dto.response.CustomerResponseDTO;
import com.sysco.dom.dto.status.CustomerRequestDTO;
import com.sysco.dom.service.business_logic.CustomerResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@CrossOrigin
@Slf4j
public class CustomerController {

    private final CustomerResponseService customerResponseService;

    @Autowired
    public CustomerController(CustomerResponseService customerResponseService) {
        this.customerResponseService = customerResponseService;
    }

    /**
     * Process customer order details and return enriched response with customer information
     *
     * @param requestDTO Contains order details and customer number
     * @return Customer response with order details and customer information
     */
    @PostMapping("/process-order")
    public ResponseEntity<CustomerResponseDTO> processCustomerOrder(@RequestBody CustomerRequestDTO requestDTO) {
        log.info("Received customer order processing request for message ID: {}", requestDTO.getMessageId());

        try {
            CustomerResponseDTO responseDTO = customerResponseService.processCustomerRequest(requestDTO);
            log.info("Successfully processed customer order for customer: {}", responseDTO.getCustomerNumber());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing customer order: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}