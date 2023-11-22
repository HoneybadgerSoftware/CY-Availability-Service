package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.factory.AvailabilityEntityFactory;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityEntityFactory availabilityEntityFactory;

    @Override
    public void updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest) {

    }
}
