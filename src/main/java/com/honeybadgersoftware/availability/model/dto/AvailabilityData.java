package com.honeybadgersoftware.availability.model.dto;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityData {

     private List<AvailabilityEntity> availableProducts;
     private List<Long> missingProductsIds;
}
