package com.honeybadgersoftware.availability.model.request;

import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductsAveragePriceRequest {

    private List<ProductAveragePriceData> data;
}
