package com.honeybadgersoftware.availability.factory;

import com.honeybadgersoftware.availability.model.UpdateAvailabilityData;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.utils.factory.ManyToOneFactory;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityEntityFactory implements ManyToOneFactory<UpdateAvailabilityData, Long, AvailabilityEntity> {

    @Override
    public AvailabilityEntity map(UpdateAvailabilityData updateAvailabilityData, Long shopId) {
        return AvailabilityEntity.builder()
                .productId(updateAvailabilityData.getProductId())
                .price(updateAvailabilityData.getPrice())
                .shopId(shopId)
                .build();
    }
}
