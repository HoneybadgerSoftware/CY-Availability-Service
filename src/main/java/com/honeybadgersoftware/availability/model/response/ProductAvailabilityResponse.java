package com.honeybadgersoftware.availability.model.response;

import com.honeybadgersoftware.availability.model.dto.ProductAvailabilityPerShopData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAvailabilityResponse {
    private List<ProductAvailabilityPerShopData> data;
}
