package com.example.mybatisbatch.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderQtyByProduct {

    @Getter
    @Setter
    private int summaryId;

    @Getter
    @Setter
    private String productId;

    @Getter
    @Setter
    private int totalOrderQty;

    @Getter
    @Setter
    private LocalDateTime createAt;

    public OrderQtyByProduct addCount() {
        totalOrderQty++;
        return this;
    }
}
