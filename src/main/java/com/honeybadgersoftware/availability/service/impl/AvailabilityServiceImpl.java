package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.model.entity.ProductIdProjection;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private static final int PAGE_SIZE = 20;


    @Override
    public Page<ProductIdProjection> getRandomProductsByShop(List<Long> shopIds) {

        int tableSize = (int) availabilityRepository.count();
        int offset = new Random().nextInt(tableSize - PAGE_SIZE + 1);

        return availabilityRepository.findRandomProductsIdsByShopIds(shopIds, PageRequest.of(offset, PAGE_SIZE));
    }
}
