package com.honeybadgersoftware.availability.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAvailabilityData {

    private Map<Long, AvailabilityData> data;
}
