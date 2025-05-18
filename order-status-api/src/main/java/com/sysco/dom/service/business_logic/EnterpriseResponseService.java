package com.sysco.dom.service.business_logic;

import com.sysco.dom.dto.response.EnterpriseResponseDTO;
import com.sysco.dom.dto.status.EnterpriseRequestDTO;

public interface EnterpriseResponseService {
    EnterpriseResponseDTO processEnterpriseRequest(EnterpriseRequestDTO requestDTO);
}
