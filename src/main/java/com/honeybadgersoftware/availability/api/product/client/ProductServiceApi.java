package com.honeybadgersoftware.availability.api.product.client;

import com.honeybadgersoftware.availability.model.entity.ProductIdProjection;
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "productService", url = "http://localhost:8081/products")
public interface ProductServiceApi {

    @PutMapping("/synchronize/existingProducts")
    ResponseEntity<Void> updateExistingProductsAveragePrice(
            @RequestBody UpdateProductsAveragePriceRequest productsAveragePriceRequest);

    @PostMapping("/display")
    ResponseEntity<Void> productsAvailabilitySynchronization(
            @RequestBody Page<ProductIdProjection> productIds);
}
