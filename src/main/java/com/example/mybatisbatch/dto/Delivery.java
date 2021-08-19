package com.example.mybatisbatch.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Getter
    @Setter
    private int deliveryId;

    @Getter
    @Setter
    private int orderId;

    @Getter
    @Setter
    private int orderQty;

    @Getter
    @Setter
    private String deliveryType;

    @Getter
    @Setter
    private LocalDateTime createAt;

    public static Delivery of(Order order) {
        Delivery result = new Delivery();
        result.setOrderId(order.getOrderId());
        result.setDeliveryType("TYPE_"+order.getProductId());
        result.setOrderQty(order.getOrderQty());

        return result;
    }
}
