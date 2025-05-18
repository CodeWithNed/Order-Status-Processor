package com.sysco.dom.service.data_access;

import com.sysco.dom.dto.response.EnterpriseResponseDTO;
import com.sysco.dom.entity.enterprise_detail;
import com.sysco.dom.repository.EnterpriseRepo;
import com.sysco.dom.service.data_access.EnterpriseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private EnterpriseRepo enterpriseRepo;

    @Autowired
    private ModelMapper modelMapper;

    public EnterpriseResponseDTO getUserByUserID(String customer_id){
        enterprise_detail enterpriseDetail  = enterpriseRepo.getEnterpriseByEnterpriseID(customer_id);
        return modelMapper.map(enterpriseDetail, EnterpriseResponseDTO.class);
    }
}
