package com.honeybadgersoftware.availability.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePrice> data;
}
