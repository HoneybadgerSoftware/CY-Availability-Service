package com.honeybadgersoftware.availability.facade;

import com.honeybadgersoftware.availability.api.product.client.ProductServiceApi;
import com.honeybadgersoftware.availability.model.request.CheckAvailabilityRequest;
import com.honeybadgersoftware.availability.model.request.GetRandomProductsByShops;
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest;
import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse;
import com.honeybadgersoftware.availability.model.response.ProductIdsResponse;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import com.honeybadgersoftware.availability.service.AvailabilityUpdateService;
import com.honeybadgersoftware.availability.service.PriceService;
import com.honeybadgersoftware.availability.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AvailabilityFacade {

    private final AvailabilityUpdateService availabilityUpdateService;
    private final PriceService priceService;
    private final VerifyService verifyService;
    private final AvailabilityService availabilityService;
    private final ProductServiceApi productServiceApi;

    public void synchronizeProductsAvailabilityData(UpdateAvailabilityRequest updateAvailabilityRequest) {

        List<Long> updateAvailabilityIds = availabilityUpdateService.updateAvailability(updateAvailabilityRequest);
        productServiceApi.updateExistingProductsAveragePrice(
                UpdateProductsAveragePriceRequest.builder()
                        .data(priceService.prepareProductsAveragePriceData(
                                updateAvailabilityIds))
                        .build());
    }

    public ProductAvailabilityResponse getProductsAvailability(CheckAvailabilityRequest checkAvailabilityRequest) {

        List<Long> shopIds = checkAvailabilityRequest.getShopIds();
        List<Long> productIds = checkAvailabilityRequest.getProductIds();
        if (shopIds.isEmpty()) {
            return verifyService.verifyAvailabilityForAllShops(productIds);
        }
        return verifyService.verifyAvailabilityForSpecificShops(
                productIds, shopIds);
    }

    public ProductIdsResponse getRandomProductsByShops(GetRandomProductsByShops getRandomProductsByShops) {
        List<Long> randomProductsByShop =
                availabilityService.getRandomProductsByShop(getRandomProductsByShops.getShopIds());
        if (randomProductsByShop.isEmpty()) {
            return new ProductIdsResponse(Collections.emptyList());
        }
        return new ProductIdsResponse(randomProductsByShop);
    }
}
