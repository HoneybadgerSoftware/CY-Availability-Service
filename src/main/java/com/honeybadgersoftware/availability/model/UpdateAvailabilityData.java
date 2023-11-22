package com.honeybadgersoftware.availability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class UpdateAvailabilityData {

    private Long id;
    private BigDecimal price;
}
