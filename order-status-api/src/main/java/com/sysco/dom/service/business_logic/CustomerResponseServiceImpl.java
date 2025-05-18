package com.sysco.dom.service.business_logic;

import com.sysco.dom.dto.response.CustomerResponseDTO;
import com.sysco.dom.dto.status.CustomerRequestDTO;
import com.sysco.dom.entity.customer_detail;
import com.sysco.dom.exceptions.CustomerNotFoundException;
import com.sysco.dom.repository.CustomerRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class CustomerResponseServiceImpl implements CustomerResponseService {

    private final CustomerRepo customerRepo;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerResponseServiceImpl(CustomerRepo customerRepo, ModelMapper modelMapper) {
        this.customerRepo = customerRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponseDTO processCustomerRequest(CustomerRequestDTO requestDTO) {
        log.info("Processing customer request with message ID: {}", requestDTO.getMessageId());

        // Get customer details from repository
        customer_detail customerDetail = customerRepo.getCustomerByCustomerID(requestDTO.getCustomerNumber());

        if (customerDetail == null) {
            throw new CustomerNotFoundException("Customer not found with ID: " + requestDTO.getCustomerNumber());
        }

        // Map request and customer details to response
        CustomerResponseDTO responseDTO = mapToResponseDTO(requestDTO, customerDetail);

        log.info("Completed processing for customer: {}", customerDetail.getCustomer_name());
        return responseDTO;
    }

    private CustomerResponseDTO mapToResponseDTO(CustomerRequestDTO requestDTO, customer_detail customerDetail) {
        // First map the request fields
        CustomerResponseDTO responseDTO = modelMapper.map(requestDTO, CustomerResponseDTO.class);

        // Add the customer details from the entity
        responseDTO.setCustomer_name(customerDetail.getCustomer_name());
        responseDTO.setCustomer_email(customerDetail.getCustomer_email());
        responseDTO.setCustomer_telephone(customerDetail.getCustomer_telephone());
        responseDTO.setCustomer_address(customerDetail.getCustomer_address());

        return responseDTO;
    }
}