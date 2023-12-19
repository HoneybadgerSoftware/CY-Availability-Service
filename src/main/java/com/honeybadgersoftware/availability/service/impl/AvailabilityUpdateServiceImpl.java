package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.component.decorator.AvailabilityEntityDecorator;
import com.honeybadgersoftware.availability.component.factory.AvailabilityEntityFactory;
import com.honeybadgersoftware.availability.model.dto.ProductPriceData;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.AvailabilityUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

@Service
@RequiredArgsConstructor
public class AvailabilityUpdateServiceImpl implements AvailabilityUpdateService {

    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityEntityDecorator availabilityEntityDecorator;
    private final AvailabilityEntityFactory availabilityEntityFactory;

    @Override
    public List<Long> updateAvailability(UpdateAvailabilityRequest updateAvailabilityRequest) {

        List<ProductPriceData> newProductsData = updateAvailabilityRequest.getNewProductsData();
        List<ProductPriceData> existingProductsData = updateAvailabilityRequest.getExistingProductsData();
        Long shopId = updateAvailabilityRequest.getShopId();

        if (newProductsData.isEmpty()) {
            List<Long> longs = updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
            System.out.println("Dupa" + longs);
            return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
        }
        addNewProducts(newProductsData, shopId);
        List<Long> longs = updateAvailabilityDataForExistingProducts(existingProductsData, shopId);

        System.out.println("Dupa" + longs);
        return updateAvailabilityDataForExistingProducts(existingProductsData, shopId);
    }

    private void addNewProducts(List<ProductPriceData> newProductsData, Long shopId) {
        availabilityRepository.saveAll(availabilityEntityFactory.map(newProductsData, shopId));
    }

    //sprawdz na existing products czy istnieje z takim shop id, to ci powoduje problem

    private List<Long> updateAvailabilityDataForExistingProducts(List<ProductPriceData> data, Long shopId) {
        return updateAvailabilityDatabase(data, shopId).stream()
                .map(AvailabilityEntity::getProductId).toList();
    }

    private List<AvailabilityEntity> updateAvailabilityDatabase(List<ProductPriceData> productPriceData, Long shopId) {

        System.out.println("Dupa1" + productPriceData);

        List<AvailabilityEntity> existingEntities = availabilityRepository.findAllByProductIdAndShopId(
                productPriceData.stream().map(ProductPriceData::getProductId).toList(),
                shopId);

        System.out.println(existingEntities);

        existingEntities.forEach(existingEntity -> {
            Optional<ProductPriceData> updateDataOptional = productPriceData.stream()
                    .filter(updateData -> matchEntityWithAvailabilityData.test(updateData, existingEntity))
                    .findFirst();

            updateDataOptional.ifPresent(updateData ->
                    availabilityEntityDecorator.decorate(updateData.getPrice(), existingEntity));
            existingEntity.setId(existingEntity.getId());
        });

        return availabilityRepository.saveAll(existingEntities);
    }

    private final BiPredicate<ProductPriceData, AvailabilityEntity> matchEntityWithAvailabilityData =
            (e1, e2) -> (e2.getProductId().equals(e1.getProductId()));


}
