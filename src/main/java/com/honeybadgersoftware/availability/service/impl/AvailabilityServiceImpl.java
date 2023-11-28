package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.decorator.AvailabilityEntityDecorator;
import com.honeybadgersoftware.availability.factory.AvailabilityEntityFactory;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityData;
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityEntityFactory availabilityEntityFactory;
    private final AvailabilityEntityDecorator availabilityEntityDecorator;

    @Override
    public List<Long> updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest) {

        List<UpdateAvailabilityData> newProductsData = updateAvailabilityRequest.getNewProductsData();
        List<UpdateAvailabilityData> existingProductsData = updateAvailabilityRequest.getExistingProductsData();
        Long shopId = updateAvailabilityRequest.getShopId();

        if (newProductsData.isEmpty()) {
            return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
        }
        updateAvailabilityDatabase(newProductsData, shopId);
        return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
    }

    private List<Long> updateAvailabilityDataForExistingProducts(List<UpdateAvailabilityData> data, Long shopId) {
        return updateAvailabilityDatabase(data, shopId).stream()
                .map(AvailabilityEntity::getProductId).collect(Collectors.toList());
    }

    private List<AvailabilityEntity> updateAvailabilityDatabase(List<UpdateAvailabilityData> updateAvailabilityData, Long shopId) {

        List<AvailabilityEntity> existingEntities = availabilityRepository.findAllByProductIdAndShopId(
                updateAvailabilityData.stream().map(UpdateAvailabilityData::getProductId).toList(),
                shopId);

        System.out.println("Dupa " + existingEntities.toString());

        List<AvailabilityEntity> updatedEntities = existingEntities.stream()
                .peek(existingEntity -> updateAvailabilityData.stream()
                        .filter(updateData -> matchEntityWithAvailabilityData.test(updateData, existingEntity))
                        .findFirst().ifPresent(
                                correspondingUpdateData ->
                                        availabilityEntityDecorator.decorate(
                                                correspondingUpdateData.getPrice(),
                                                existingEntity)))
                .collect(Collectors.toList());

        return availabilityRepository.saveAll(updatedEntities);
    }

    private final BiPredicate<UpdateAvailabilityData, AvailabilityEntity> matchEntityWithAvailabilityData =
            (e1, e2) -> (e2.getProductId().equals(e1.getProductId()));


}
