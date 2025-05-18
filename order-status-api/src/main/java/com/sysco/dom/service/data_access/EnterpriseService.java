package com.sysco.dom.service.data_access;

import com.sysco.dom.dto.response.EnterpriseResponseDTO;

public interface EnterpriseService {
    EnterpriseResponseDTO getUserByUserID(String customer_id);
}
