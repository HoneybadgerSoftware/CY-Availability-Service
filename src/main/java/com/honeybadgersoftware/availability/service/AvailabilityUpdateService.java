package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;

import java.util.List;

public interface AvailabilityUpdateService {

    List<Long> updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest);
}
