package com.honeybadgersoftware.availability.controller

import com.honeybadgersoftware.availability.base.BaseIntegrationTest
import com.honeybadgersoftware.availability.model.UpdateAvailabilityData
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AvailabilityControllerITest extends BaseIntegrationTest{

    def "updateAvailability updates product when valid request"() {
        given:
        Long shopId = 1L
        UpdateAvailabilityData existingProductData = new UpdateAvailabilityData(1L, new BigDecimal("19.99"))
        UpdateAvailabilityData newProductData = new UpdateAvailabilityData(2L, new BigDecimal("29.99"))
        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest(shopId, [existingProductData], [newProductData])

        when:
        ResponseEntity<Void> response = restTemplate.put(addressToUseForTests + "/availability/update", request, Void.class)

        then:
        response.statusCode == HttpStatus.OK

    }
}
