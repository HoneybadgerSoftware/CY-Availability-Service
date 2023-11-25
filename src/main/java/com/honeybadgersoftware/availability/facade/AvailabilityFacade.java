package com.honeybadgersoftware.availability.facade;


import com.honeybadgersoftware.availability.api.product.client.ProductServiceApi;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.UpdateProductsAveragePriceRequest;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import com.honeybadgersoftware.availability.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityFacade {

    private final AvailabilityService availabilityService;
    private final PriceService priceService;
    private final ProductServiceApi productServiceApi;

    public void synchronizeProductsAvailabilityData(UpdateAvailabilityRequest updateAvailabilityRequest) {
        productServiceApi.updateExistingProductsAveragePrice(
                UpdateProductsAveragePriceRequest.builder()
                        .data(priceService.prepareProductsAveragePriceData(
                                availabilityService.updateAvailability(updateAvailabilityRequest)))
                        .build());
    }

}
