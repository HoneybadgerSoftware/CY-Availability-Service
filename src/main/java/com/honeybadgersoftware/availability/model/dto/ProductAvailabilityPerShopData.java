package com.honeybadgersoftware.availability.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAvailabilityPerShopData {

    private Long shopId;
    private BigDecimal totalPriceOfProducts;
    private List<ProductPriceData> productsPrices;

    private List<Long> missingProducts;

}
