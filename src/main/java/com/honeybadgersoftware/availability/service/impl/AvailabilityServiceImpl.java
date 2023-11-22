package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.factory.AvailabilityEntityFactory;
import com.honeybadgersoftware.availability.model.ProductAveragePrice;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityEntityFactory availabilityEntityFactory;

    @Override
    public void updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest) {


        if(updateAvailabilityRequest.getNewProductsData().isEmpty()) {
            List<AvailabilityEntity> entities =
                    availabilityEntityFactory.map(
                            updateAvailabilityRequest.getExistingProductsData(),
                            updateAvailabilityRequest.getShopId());
            availabilityRepository.saveAll(entities);

            List<Long> collect = entities.stream().map(AvailabilityEntity::getId).toList();

            Map<Long, List<AvailabilityEntity>> productsMap = availabilityRepository
                    .findAllByProductIds(collect)
                    .stream()
                    .collect(Collectors.groupingBy(AvailabilityEntity::getProductId));

            List<ProductAveragePrice> productAveragePrices = productsMap.entrySet()
                    .stream()
                    .map(entry -> {
                        Long id = entry.getKey();
                        List<AvailabilityEntity> products = entry.getValue();

                        BigDecimal averagePrice = products.stream()
                                .map(AvailabilityEntity::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .divide(BigDecimal.valueOf(products.size()), 2, RoundingMode.HALF_UP);

                        return new ProductAveragePrice(id, averagePrice);
                    })
                    .toList();
        }
    }
}
