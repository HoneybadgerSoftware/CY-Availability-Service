package com.honeybadgersoftware.availability.controller;

import com.honeybadgersoftware.availability.facade.AvailabilityFacade;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityFacade facade;

    @PutMapping("/update")
    ResponseEntity<Void> updateAvailability(@RequestBody UpdateAvailabilityRequest updateAvailabilityRequest) {
        return null;
    }

}
