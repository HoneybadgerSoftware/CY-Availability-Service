package com.honeybadgersoftware.availability.model.request;

import com.honeybadgersoftware.availability.model.dto.ProductPriceData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateAvailabilityRequest {
    private Long shopId;
    private List<ProductPriceData> existingProductsData;
    private List<ProductPriceData> newProductsData;
}
