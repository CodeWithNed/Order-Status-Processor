package com.sysco.dom.service.business_logic;

import com.sysco.dom.dto.response.EnterpriseResponseDTO;
import com.sysco.dom.dto.status.EnterpriseRequestDTO;
import com.sysco.dom.entity.enterprise_detail;
import com.sysco.dom.exceptions.CustomerNotFoundException;
import com.sysco.dom.repository.EnterpriseRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class EnterpriseResponseServiceImpl implements EnterpriseResponseService {

    private final EnterpriseRepo enterpriseRepo;
    private ModelMapper modelMapper;

    @Autowired
    public EnterpriseResponseServiceImpl(EnterpriseRepo enterpriseRepo, ModelMapper modelMapper) {
        this.enterpriseRepo = enterpriseRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public EnterpriseResponseDTO processEnterpriseRequest(EnterpriseRequestDTO requestDTO) {
        log.info("Processing enterprise request with message ID: {}", requestDTO.getMessageId());

        // Get enterprise details from repository
        enterprise_detail enterpriseDetail = enterpriseRepo.getEnterpriseByEnterpriseID(requestDTO.getCustomerNumber());

        if (enterpriseDetail == null) {
            throw new CustomerNotFoundException("Enterprise not found with ID: " + requestDTO.getCustomerNumber());
        }

        // Map request and enterprise details to response
        EnterpriseResponseDTO responseDTO = mapToResponseDTO(requestDTO, enterpriseDetail);

        log.info("Completed processing for enterprise: {}", enterpriseDetail.getCustomer_name());
        return responseDTO;
    }

    private EnterpriseResponseDTO mapToResponseDTO(EnterpriseRequestDTO requestDTO, enterprise_detail enterpriseDetail) {
        // First map the request fields
        EnterpriseResponseDTO responseDTO = modelMapper.map(requestDTO, EnterpriseResponseDTO.class);

        // Add the enterprise details from the entity
        responseDTO.setCustomer_name(enterpriseDetail.getCustomer_name());
        responseDTO.setCustomer_email(enterpriseDetail.getCustomer_email());
        responseDTO.setCustomer_telephone(enterpriseDetail.getCustomer_telephone());
        responseDTO.setCustomer_address(enterpriseDetail.getCustomer_address());

        return responseDTO;
    }
}