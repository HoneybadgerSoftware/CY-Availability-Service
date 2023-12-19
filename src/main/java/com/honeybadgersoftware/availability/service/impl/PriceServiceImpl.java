package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.PriceService;
import com.honeybadgersoftware.availability.component.sorter.SorterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final AvailabilityRepository availabilityRepository;
    private final SorterFactory sorterFactory;

    @Override
    public List<ProductAveragePriceData> prepareProductsAveragePriceData(List<Long> productIds) {
        return createProductsAveragePrices(groupProductsByProductId(productIds));
    }

    private List<ProductAveragePriceData> createProductsAveragePrices(Map<Long, List<AvailabilityEntity>> products) {

        System.out.println(products);

        List<ProductAveragePriceData> productAveragePriceData = products.entrySet()
                .stream()
                .map(entry ->
                        new ProductAveragePriceData(entry.getKey(), calculateProductAveragePrice(entry.getValue()))
                )
                .toList();

        System.out.println(productAveragePriceData);

        return productAveragePriceData;
    }

    private Map<Long, List<AvailabilityEntity>> groupProductsByProductId(List<Long> productIds) {
        return sorterFactory.sort(getProductsFromEveryShop(productIds), AvailabilityEntity::getProductId);
    }

    private List<AvailabilityEntity> getProductsFromEveryShop(List<Long> ids) {
        return availabilityRepository.findAllByProductIds(ids);
    }

    private BigDecimal calculateProductAveragePrice(List<AvailabilityEntity> entities) {
        return entities.stream()
                .map(AvailabilityEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(entities.size()), 2, RoundingMode.HALF_UP);
    }

}
