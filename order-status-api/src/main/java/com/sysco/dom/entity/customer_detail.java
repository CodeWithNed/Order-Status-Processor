package com.sysco.dom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Immutable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class customer_detail {

    @Id
    @Column(name="customer_id")
    private String customer_id;

    @Column(name ="customer_name")
    private String customer_name;

    @Column(name ="email")
    private String customer_email;

    @Column(name ="telephone")
    private String customer_telephone;

    @Column(name ="address")
    private String customer_address;

}
