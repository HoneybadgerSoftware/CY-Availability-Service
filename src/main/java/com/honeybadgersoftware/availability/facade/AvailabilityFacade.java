package com.honeybadgersoftware.availability.facade;

import com.honeybadgersoftware.availability.api.product.client.ProductServiceApi;
import com.honeybadgersoftware.availability.model.dto.ProductIndexesPage;
import com.honeybadgersoftware.availability.model.request.CheckAvailabilityRequest;
import com.honeybadgersoftware.availability.model.request.GetRandomProductsByShops;
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest;
import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import com.honeybadgersoftware.availability.service.AvailabilityUpdateService;
import com.honeybadgersoftware.availability.service.PriceService;
import com.honeybadgersoftware.availability.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        productServiceApi.updateExistingProductsAveragePrice(
                UpdateProductsAveragePriceRequest.builder()
                        .data(priceService.prepareProductsAveragePriceData(
                                availabilityUpdateService.updateAvailability(updateAvailabilityRequest)))
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

    public void getRandomProductsByShops(GetRandomProductsByShops getRandomProductsByShops) {
        productServiceApi.productsAvailabilitySynchronization(ProductIndexesPage.builder()
                .data(availabilityService.getRandomProductsByShop(getRandomProductsByShops.getShopIds()))
                .build());
    }
}
