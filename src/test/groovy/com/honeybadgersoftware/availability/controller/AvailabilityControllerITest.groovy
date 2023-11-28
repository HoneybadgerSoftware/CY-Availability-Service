package com.honeybadgersoftware.availability.controller

import com.honeybadgersoftware.availability.base.BaseIntegrationTest
import com.honeybadgersoftware.availability.data.ProductDataCreator
import com.honeybadgersoftware.availability.model.UpdateAvailabilityData
import com.honeybadgersoftware.availability.model.UpdateAvailabilityRequest
import com.honeybadgersoftware.availability.repository.AvailabilityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import static com.github.tomakehurst.wiremock.client.WireMock.*

class AvailabilityControllerITest extends BaseIntegrationTest{

    @Autowired
    AvailabilityRepository availabilityRepository

    def productDataCreator = new ProductDataCreator()

    def "updateAvailability updates product when valid request"() {
        given:
        Long shopId = 1L

        and:
        wireMock.stubFor(put(urlEqualTo("/products/synchronize/existingProducts"))
                .withRequestBody(equalToJson(productDataCreator.updateProductsPricesJson))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")))

        def productsBeforeUpdate = availabilityRepository.findAllByProductIds([1L, 2L])

        UpdateAvailabilityData existingProductData = new UpdateAvailabilityData(1L, new BigDecimal("19.99"))
        UpdateAvailabilityData newProductData = new UpdateAvailabilityData(2L, new BigDecimal("29.99"))
        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest(shopId, [existingProductData], [newProductData])

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<UpdateAvailabilityRequest> requestEntity = new HttpEntity<>(request, headers)

        when:
        ResponseEntity<Void> response = restTemplate.exchange(
                addressToUseForTests + "/availability/update",
                HttpMethod.PUT,
                requestEntity,
                Void.class)

        then:
        response.getStatusCode() == HttpStatus.OK

        and: "Check if products state changed"
        def productsAfterUpdate = availabilityRepository.findAllByProductIds([1L, 2L])

        productsBeforeUpdate != productsAfterUpdate

        println(productsBeforeUpdate)
        println(productsAfterUpdate)
    }
}
