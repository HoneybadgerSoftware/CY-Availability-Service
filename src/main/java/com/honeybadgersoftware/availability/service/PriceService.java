package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData;

import java.util.List;

public interface PriceService {

    List<ProductAveragePriceData> prepareProductsAveragePriceData(List<Long> productIds);

}
