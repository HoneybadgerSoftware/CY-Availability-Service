package com.honeybadgersoftware.availability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePrice> data;
}
