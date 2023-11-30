package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.component.decorator.AvailabilityEntityDecorator;
import com.honeybadgersoftware.availability.model.dto.ProductPriceData;
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityUpdateServiceImpl implements AvailabilityUpdateService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityEntityDecorator availabilityEntityDecorator;

    @Override
    public List<Long> updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest) {

        List<ProductPriceData> newProductsData = updateAvailabilityRequest.getNewProductsData();
        List<ProductPriceData> existingProductsData = updateAvailabilityRequest.getExistingProductsData();
        Long shopId = updateAvailabilityRequest.getShopId();

        if (newProductsData.isEmpty()) {
            return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
        }
        updateAvailabilityDatabase(newProductsData, shopId);
        return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
    }

    private List<Long> updateAvailabilityDataForExistingProducts(List<ProductPriceData> data, Long shopId) {
        return updateAvailabilityDatabase(data, shopId).stream()
                .map(AvailabilityEntity::getProductId).collect(Collectors.toList());
    }

    private List<AvailabilityEntity> updateAvailabilityDatabase(List<ProductPriceData> productPriceData, Long shopId) {

        List<AvailabilityEntity> existingEntities = availabilityRepository.findAllByProductIdAndShopId(
                productPriceData.stream().map(ProductPriceData::getProductId).toList(),
                shopId);

        List<AvailabilityEntity> updatedEntities = existingEntities.stream()
                .peek(existingEntity -> productPriceData.stream()
                        .filter(updateData -> matchEntityWithAvailabilityData.test(updateData, existingEntity))
                        .findFirst().ifPresent(
                                correspondingUpdateData ->
                                        availabilityEntityDecorator.decorate(
                                                correspondingUpdateData.getPrice(),
                                                existingEntity)))
                .collect(Collectors.toList());

        return availabilityRepository.saveAll(updatedEntities);
    }

    private final BiPredicate<ProductPriceData, AvailabilityEntity> matchEntityWithAvailabilityData =
            (e1, e2) -> (e2.getProductId().equals(e1.getProductId()));


}
