package com.example.mybatisbatch.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Getter
    @Setter
    private int orderId;

    @Getter
    @Setter
    private String productId;

    @Getter
    @Setter
    private int orderQty;

    @Getter
    @Setter
    private LocalDateTime createAt;
}
