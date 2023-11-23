package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.ProductAveragePrice;

import java.util.List;

public interface PriceService {

    List<ProductAveragePrice> prepareProductsAveragePriceData(List<Long> productIds);
}
