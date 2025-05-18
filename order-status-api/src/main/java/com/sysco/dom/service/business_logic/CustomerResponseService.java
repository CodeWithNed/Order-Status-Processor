package com.sysco.dom.service.business_logic;

import com.sysco.dom.dto.response.CustomerResponseDTO;
import com.sysco.dom.dto.status.CustomerRequestDTO;

public interface CustomerResponseService {

    CustomerResponseDTO processCustomerRequest(CustomerRequestDTO customerRequestDTO);
}
