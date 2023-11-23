package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;

import java.util.List;

public interface AvailabilityService {

    List<Long> updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest);
}
