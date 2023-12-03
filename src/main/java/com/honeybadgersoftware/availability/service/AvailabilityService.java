package com.honeybadgersoftware.availability.service;

import com.honeybadgersoftware.availability.model.entity.ProductIdProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AvailabilityService {

    Page<ProductIdProjection> getRandomProductsByShop(List<Long> shopIds);

}
