package com.sysco.dom.service.data_access;

import com.sysco.dom.dto.response.CustomerResponseDTO;
import com.sysco.dom.entity.customer_detail;
import com.sysco.dom.repository.CustomerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerInfoServiceImpl implements CustomerInfoService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    public CustomerResponseDTO getUserDetailsByUserID(String customerId){
        customer_detail customerDetail  = customerRepo.getCustomerByCustomerID(customerId);
        return modelMapper.map(customerDetail, CustomerResponseDTO.class);
    }
}
