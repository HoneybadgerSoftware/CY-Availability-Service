package com.honeybadgersoftware.availability.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateAvailabilityRequest {
    private Long shopId;
    private List<UpdateAvailabilityData> existingProductsData;
    private List<UpdateAvailabilityData> newProductsData;
}
