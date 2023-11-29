package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse;

import java.util.List;

public interface VerifyService {

    ProductAvailabilityResponse verifyAvailabilityForAllShops(List<Long> baseProductIds);

    ProductAvailabilityResponse verifyAvailabilityForSpecificShops(List<Long> productIds, List<Long> shopIds);
}
