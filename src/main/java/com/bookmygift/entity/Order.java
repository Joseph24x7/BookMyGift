package com.bookmygift.entity;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bookmygift.info.GiftType;
import com.bookmygift.info.OrderStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Document(collection = "OrderInfo")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String orderId;

    @Indexed(unique = true)
    private String username;

    private String emailId;

    private GiftType giftType;

    private Double amountPaid;

    private OrderStatus orderStatus;


    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId, 
                 @JsonProperty("username") String username, 
                 @JsonProperty("emailId") String emailId, 
                 @JsonProperty("giftType") GiftType giftType, 
                 @JsonProperty("amountPaid") Double amountPaid, 
                 @JsonProperty("orderStatus") OrderStatus orderStatus
                ) {
        this.orderId = orderId;
        this.username = username;
        this.emailId = emailId;
        this.giftType = giftType;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }
    
    @JsonCreator
    public Order(String json) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(json, Order.class);
        this.orderId = order.getOrderId();
        this.username = order.getUsername();
        this.emailId = order.getEmailId();
        this.giftType = order.getGiftType();
        this.amountPaid = order.getAmountPaid();
        this.orderStatus = order.getOrderStatus();
    }

}

