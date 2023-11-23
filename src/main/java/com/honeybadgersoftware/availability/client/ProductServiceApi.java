package com.honeybadgersoftware.availability.client;

import com.honeybadgersoftware.availability.model.UpdateProductsAveragePriceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "availabilityService", url = "http://localhost:8081/availability")
public interface ProductServiceApi {

    @PutMapping("/synchronize/existingProducts")
    ResponseEntity<Void> updateExistingProductsAveragePrice(
            @RequestBody UpdateProductsAveragePriceRequest productsAveragePriceRequest);
}
