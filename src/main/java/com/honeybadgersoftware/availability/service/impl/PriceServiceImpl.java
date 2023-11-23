package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.model.ProductAveragePrice;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final AvailabilityRepository availabilityRepository;

    @Override
    public List<ProductAveragePrice> prepareProductsAveragePriceData(List<Long> productIds) {
        return createProductsAveragePrices(getProductsFromEveryShop(productIds));
    }

    private List<ProductAveragePrice> createProductsAveragePrices(Map<Long, List<AvailabilityEntity>> products) {
        return products.entrySet()
                .stream()
                .map(entry ->
                        new ProductAveragePrice(entry.getKey(), calculateProductAveragePrice(entry.getValue()))
                )
                .toList();
    }

    private Map<Long, List<AvailabilityEntity>> getProductsFromEveryShop(List<Long> ids) {
        return availabilityRepository
                .findAllByProductIds(ids)
                .stream()
                .collect(Collectors.groupingBy(AvailabilityEntity::getProductId));
    }

    private BigDecimal calculateProductAveragePrice(List<AvailabilityEntity> entities) {
        return entities.stream()
                .map(AvailabilityEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(entities.size()), 2, RoundingMode.HALF_UP);
    }
}
