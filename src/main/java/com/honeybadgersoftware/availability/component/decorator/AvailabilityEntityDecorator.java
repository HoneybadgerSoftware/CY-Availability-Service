package com.honeybadgersoftware.availability.component.decorator;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.utils.decorator.GenericDecorator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AvailabilityEntityDecorator implements GenericDecorator<BigDecimal, AvailabilityEntity> {
    @Override
    public AvailabilityEntity decorate(BigDecimal price, AvailabilityEntity availabilityEntity) {
        availabilityEntity.setPrice(price);
        return availabilityEntity;
    }
}
