package com.sysco.dom.service.data_access;

import com.sysco.dom.dto.response.CustomerResponseDTO;

public interface CustomerInfoService {
    CustomerResponseDTO getUserDetailsByUserID(String customerId);
}
