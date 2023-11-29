package com.honeybadgersoftware.availability.controller

import com.honeybadgersoftware.availability.base.BaseIntegrationTest
import com.honeybadgersoftware.availability.data.ProductDataCreator
import com.honeybadgersoftware.availability.model.dto.ProductAvailabilityPerShopData
import com.honeybadgersoftware.availability.model.dto.ProductPriceData
import com.honeybadgersoftware.availability.model.request.CheckAvailabilityRequest
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

//    def "updateAvailability updates product when valid request"() {
//        given:
//        Long shopId = 1L
//
//        and:
//        wireMock.stubFor(put(urlEqualTo("/products/synchronize/existingProducts"))
//                .withRequestBody(equalToJson(productDataCreator.updateProductsPricesJson))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")))
//
//        def productsBeforeUpdate = availabilityRepository.findAllByProductIds([1L, 2L])
//
//        ProductPriceData existingProductData = new ProductPriceData(1L, new BigDecimal("19.99"))
//        ProductPriceData newProductData = new ProductPriceData(2L, new BigDecimal("29.99"))
//        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest(shopId, [existingProductData], [newProductData])
//
//        HttpHeaders headers = new HttpHeaders()
//        headers.setContentType(MediaType.APPLICATION_JSON)
//        HttpEntity<UpdateAvailabilityRequest> requestEntity = new HttpEntity<>(request, headers)
//
//        when:
//        ResponseEntity<Void> response = restTemplate.exchange(
//                addressToUseForTests + "/availability/update",
//                HttpMethod.PUT,
//                requestEntity,
//                Void.class)
//
//        then:
//        response.getStatusCode() == HttpStatus.OK
//
//        and: "Check if products state changed"
//        def productsAfterUpdate = availabilityRepository.findAllByProductIds([1L, 2L])
//
//        productsBeforeUpdate != productsAfterUpdate
//
//        println(productsBeforeUpdate)
//        println(productsAfterUpdate)
//    }

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

//        assert responseDataList.size() >= 3 : "Oczekiwano co najmniej 3 elementów w odpowiedzi"

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
    //bład wali ci dlatego że sam sie przed tym zabezpieczyłeś żeby nie iterować całej db, musisz wymyślić
    // obejście jeśli chcesz zwrócić z kilku sklepów niepełne listy (możesz na fasadzie zrobić walidacje i walnąć exception
    //z lista ID ktorych nie ma w innych sklepach

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
