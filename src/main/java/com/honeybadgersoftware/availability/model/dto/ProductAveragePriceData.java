package com.honeybadgersoftware.availability.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductAveragePriceData {
    private Long productId;
    private BigDecimal averagePrice;
}
