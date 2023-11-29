package com.honeybadgersoftware.availability.controller;

import com.honeybadgersoftware.availability.facade.AvailabilityFacade;
import com.honeybadgersoftware.availability.model.request.CheckAvailabilityRequest;
import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse;
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityFacade facade;

    @PutMapping("/update")
    ResponseEntity<Void> updateAvailability(@RequestBody UpdateAvailabilityRequest updateAvailabilityRequest) {
        facade.synchronizeProductsAvailabilityData(updateAvailabilityRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    ResponseEntity<ProductAvailabilityResponse> getProductsAvailability(@RequestBody CheckAvailabilityRequest checkAvailabilityRequest) {
        return ResponseEntity.ok(facade.getProductsAvailability(checkAvailabilityRequest));
    }

}
