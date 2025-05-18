package com.sysco.dom.repository;

import com.sysco.dom.entity.enterprise_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnterpriseRepo extends JpaRepository<enterprise_detail, Integer> {

    @Query(value = "SELECT * FROM enterprise_detail WHERE customer_id = ?1", nativeQuery = true)
    enterprise_detail getEnterpriseByEnterpriseID(String enterprise_id);
}
