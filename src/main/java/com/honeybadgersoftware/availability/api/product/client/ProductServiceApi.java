package com.honeybadgersoftware.availability.api.product.client;

import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "productService", url = "http://localhost:8081/products")
public interface ProductServiceApi {

    @PutMapping("/synchronize/existingProducts")
    @Headers("Content-Type: application/json")
    ResponseEntity<Void> updateExistingProductsAveragePrice(
            @RequestBody UpdateProductsAveragePriceRequest productsAveragePriceRequest);

}
