package com.bookmygift.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "ORDER", schema = "myapp")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID", unique = true)
    private String orderId;

    @Column(name = "USERNAME")
    private String username;

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
                 @JsonProperty("username") String username,
                 @JsonProperty("emailId") String emailId,
                 @JsonProperty("giftType") GiftTypeEnum giftType,
                 @JsonProperty("amountPaid") Double amountPaid,
                 @JsonProperty("orderStatus") OrderStatusEnum orderStatus
    ) {
        this.orderId = orderId;
        this.username = username;
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
        this.username = order.getUsername();
        this.emailId = order.getEmailId();
        this.giftType = order.getGiftType();
        this.amountPaid = order.getAmountPaid();
        this.orderStatus = order.getOrderStatus();
    }

    public Order() {

    }

    public Order(Long id, String orderId, String username, String emailId, GiftTypeEnum giftType, Double amountPaid, OrderStatusEnum orderStatus) {
        this.id = id;
        this.orderId = orderId;
        this.username = username;
        this.emailId = emailId;
        this.giftType = giftType;
        this.amountPaid = amountPaid;
        this.orderStatus = orderStatus;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public GiftTypeEnum getGiftType() {
        return this.giftType;
    }

    public Double getAmountPaid() {
        return this.amountPaid;
    }

    public OrderStatusEnum getOrderStatus() {
        return this.orderStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setGiftType(GiftTypeEnum giftType) {
        this.giftType = giftType;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String toString() {
        return "Order(id=" + this.getId() + ", orderId=" + this.getOrderId() + ", username=" + this.getUsername()  + ", giftType=" + this.getGiftType() + ", amountPaid=" + this.getAmountPaid() + ", orderStatus=" + this.getOrderStatus() + ")";
    }

    public static class OrderBuilder {
        private Long id;
        private String orderId;
        private String username;
        private String emailId;
        private GiftTypeEnum giftType;
        private Double amountPaid;
        private OrderStatusEnum orderStatus;

        OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder username(String username) {
            this.username = username;
            return this;
        }

        public OrderBuilder emailId(String emailId) {
            this.emailId = emailId;
            return this;
        }

        public OrderBuilder giftType(GiftTypeEnum giftType) {
            this.giftType = giftType;
            return this;
        }

        public OrderBuilder amountPaid(Double amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatusEnum orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Order build() {
            return new Order(this.id, this.orderId, this.username, this.emailId, this.giftType, this.amountPaid, this.orderStatus);
        }

        public String toString() {
            return "Order.OrderBuilder(id=" + this.id + ", orderId=" + this.orderId + ", username=" + this.username + ", emailId=" + this.emailId + ", giftType=" + this.giftType + ", amountPaid=" + this.amountPaid + ", orderStatus=" + this.orderStatus + ")";
        }
    }
}

