package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public void getRandomProductsByShop(List<Long> shopIds) {

        int liczbaRekordow = (int) availabilityRepository.count();
        int losowyOffset = new Random().nextInt(liczbaRekordow - rozmiarStrony + 1);


        availabilityRepository.findRandomProductsIdsByShopIds(shopIds,   );
    }
}
