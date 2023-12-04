package com.honeybadgersoftware.availability.service;

import java.util.List;

public interface AvailabilityService {

    List<Long> getRandomProductsByShop(List<Long> shopIds);

}
