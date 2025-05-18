package com.sysco.dom.repository;

import com.sysco.dom.entity.customer_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepo extends JpaRepository<customer_detail, Integer> {

    @Query(value = "SELECT * FROM customer_detail WHERE customer_id = ?1", nativeQuery = true)
    customer_detail getCustomerByCustomerID(String customer_id);
}
