package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private static final int PAGE_SIZE = 20;


    @Override
    public List<Long> getRandomProductsByShop(List<Long> shopIds) {

        int tableSize = (int) availabilityRepository.count();
        int seed = tableSize - PAGE_SIZE + 1;
        if (seed <= 0) {
            return availabilityRepository.findRandomProductsIdsByShopIds(shopIds, PageRequest.of(0, PAGE_SIZE));
        }
        int offset = new SecureRandom().nextInt(seed);

        return availabilityRepository.findRandomProductsIdsByShopIds(shopIds, PageRequest.of(offset, PAGE_SIZE));
    }
}
