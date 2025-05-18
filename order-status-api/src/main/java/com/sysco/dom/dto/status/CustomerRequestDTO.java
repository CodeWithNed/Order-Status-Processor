package com.sysco.dom.dto.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerRequestDTO {

    // from request
    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("customer_number")
    private String customerNumber;

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("delivery_date")
    private String deliveryDate;

    @JsonProperty("delivery_method")
    private String deliveryMethod;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("total_price")
    private String totalPrice;

    @JsonProperty("order_date")
    private String orderDate;

    @JsonProperty("created_timestamp")
    private String createdTimestamp;

    @JsonProperty("updated_timestamp")
    private String updatedTimestamp;
}
