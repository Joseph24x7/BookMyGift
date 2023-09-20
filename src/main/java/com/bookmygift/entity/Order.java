package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "ORDER", schema = "myapp")
@Data
@Builder
@NoArgsConstructor
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID", unique = true)
    private String orderId;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "GIFT_TYPE")
    @Enumerated(EnumType.STRING)
    private GiftTypeEnum giftType;

    @Column(name = "AMOUNT_PAID")
    private Double amountPaid;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId,
                 @JsonProperty("emailId") String emailId,
                 @JsonProperty("giftType") GiftTypeEnum giftType,
                 @JsonProperty("amountPaid") Double amountPaid,
                 @JsonProperty("orderStatus") OrderStatusEnum orderStatus
    ) {
        this.orderId = orderId;
        this.emailId = emailId;
        this.giftType = giftType;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    @JsonCreator
    public Order(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(json, Order.class);
        this.orderId = order.getOrderId();
        this.emailId = order.getEmailId();
        this.giftType = order.getGiftType();
        this.amountPaid = order.getAmountPaid();
        this.orderStatus = order.getOrderStatus();
    }

    public Order(Long id, String orderId, String emailId, GiftTypeEnum giftType, Double amountPaid, OrderStatusEnum orderStatus) {
        this.id = id;
        this.orderId = orderId;
        this.emailId = emailId;
        this.giftType = giftType;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

}

