package com.honeybadgersoftware.availability.component.factory;

import com.honeybadgersoftware.availability.model.dto.ProductPriceData;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.utils.factory.ManyToOneFactory;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityEntityFactory implements ManyToOneFactory<ProductPriceData, Long, AvailabilityEntity> {

    @Override
    public AvailabilityEntity map(ProductPriceData productPriceData, Long shopId) {
        return AvailabilityEntity.builder()
                .productId(productPriceData.getProductId())
                .price(productPriceData.getPrice())
                .shopId(shopId)
                .build();
    }
}
