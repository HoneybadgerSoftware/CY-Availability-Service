package com.honeybadgersoftware.availability.controller

import com.honeybadgersoftware.availability.base.BaseIntegrationTest
import com.honeybadgersoftware.availability.data.ProductDataCreator
import com.honeybadgersoftware.availability.data.SimplePage
import com.honeybadgersoftware.availability.model.dto.ProductAvailabilityPerShopData
import com.honeybadgersoftware.availability.model.dto.ProductPriceData
import com.honeybadgersoftware.availability.model.request.CheckAvailabilityRequest
import com.honeybadgersoftware.availability.model.request.GetRandomProductsByShops
import com.honeybadgersoftware.availability.model.request.UpdateAvailabilityRequest
import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse
import com.honeybadgersoftware.availability.repository.AvailabilityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*

import java.math.RoundingMode

import static com.github.tomakehurst.wiremock.client.WireMock.*

class AvailabilityControllerITest extends BaseIntegrationTest {

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

        def productsBeforeUpdate = availabilityRepository.findAllByProductIds([9L, 2L])

        ProductPriceData existingProductData = new ProductPriceData(9L, new BigDecimal("19.99"))
        ProductPriceData newProductData = new ProductPriceData(2L, new BigDecimal("29.99"))
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
        def productsAfterUpdate = availabilityRepository.findAllByProductIds([9L, 2L])

        productsBeforeUpdate != productsAfterUpdate

        println(productsBeforeUpdate)
        println(productsAfterUpdate)
    }

    def "getProductsAvailability returns correct data"() {
        given:
        CheckAvailabilityRequest checkAvailabilityRequest = new CheckAvailabilityRequest(productsIds, shopId)
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<CheckAvailabilityRequest> requestEntity = new HttpEntity<>(checkAvailabilityRequest, headers)

        when:
        ResponseEntity<ProductAvailabilityResponse> response = restTemplate.exchange(
                addressToUseForTests + "/availability/check",
                HttpMethod.GET,
                requestEntity,
                ProductAvailabilityResponse.class)

        then:
        response.getStatusCode() == HttpStatus.OK

        and: "Verify the response body"
        println(response)

        def responseDataList = response.body.data
        def expectedDataList = expectedResponse.getData()

        //I know iterating over response in test isnt the best practice, everything should be printed
        //but with my low pc (that i have been working on this service)
        //it is quite optimal(I do not have to write 2 separate tests), and also with this approach I can show some skills
        responseDataList.eachWithIndex { responseData, index ->
            if (index < expectedDataList.size()) {
                with(responseData) {
                    def expectedResponseData = expectedDataList.get(index)
                     getShopId() == expectedResponseData.getShopId()
                     totalPriceOfProducts == expectedResponseData.totalPriceOfProducts
                     productsPrices == expectedResponseData.productsPrices
                     missingProducts == expectedResponseData.missingProducts
                }
            }
        }


        where:
        shopId   | productsIds  | expectedResponse
        []       | [1L, 5L, 6L] | partOfAvailabilityResponseCreatorForEmptyShopListWhenCantCompleteAnyList()
        [1L, 2L] | [1L, 7L]     | availabilityResponseWithSpecifiedShops()
    }


    def 'should return response with random products ids per shop'(){

        given:
        def shops = new GetRandomProductsByShops([1L, 2L])
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<GetRandomProductsByShops> requestEntity = new HttpEntity<>(shops, headers)

        and:
        wireMock.stubFor(post(urlEqualTo("/products/display"))
                .withRequestBody(equalToJson(SimplePage.json))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")))

        when:
        ResponseEntity<Void> response = restTemplate.exchange(
                addressToUseForTests + "/availability/check/random",
                HttpMethod.GET,
                requestEntity,
                Void.class)

        then:
        response.getStatusCode() == HttpStatus.OK
    }

    static def partOfAvailabilityResponseCreatorForEmptyShopListWhenCantCompleteAnyList() {
        return new ProductAvailabilityResponse(List.of(
                ProductAvailabilityPerShopData.builder()
                        .shopId(1L)
                        .totalPriceOfProducts(BigDecimal.valueOf(94.98).setScale(2, RoundingMode.HALF_UP))
                        .productsPrices(List.of(new ProductPriceData(1L, 49.99), new ProductPriceData(6L, 44.99)))
                        .missingProducts(List.of(5L))
                        .build()))
    }

    static def availabilityResponseWithSpecifiedShops() {
        return new ProductAvailabilityResponse(List.of(
                ProductAvailabilityPerShopData.builder()
                        .shopId(1L)
                        .totalPriceOfProducts(BigDecimal.valueOf(64.98).setScale(2, RoundingMode.HALF_UP))
                        .productsPrices(List.of(new ProductPriceData(1L, 49.99), new ProductPriceData(7L, 14.99)))
                        .missingProducts(Collections.emptyList())
                        .build(),
                ProductAvailabilityPerShopData.builder()
                        .shopId(2L)
                        .totalPriceOfProducts(BigDecimal.valueOf(64.98).setScale(2, RoundingMode.HALF_UP))
                        .productsPrices(List.of(new ProductPriceData(1L, 29.99), new ProductPriceData(7L, 34.99)))
                        .missingProducts(Collections.emptyList())
                        .build()))
    }
}
