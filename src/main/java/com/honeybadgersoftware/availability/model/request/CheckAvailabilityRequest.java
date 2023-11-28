package com.honeybadgersoftware.availability.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckAvailabilityRequest {

    private List<Long> productIds;
    private List<Long> ShopIds;

}
