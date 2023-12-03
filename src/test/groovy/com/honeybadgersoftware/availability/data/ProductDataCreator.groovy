package com.honeybadgersoftware.availability.data

import com.google.gson.Gson
import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData
import com.honeybadgersoftware.availability.model.entity.ProductIdProjection
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

import java.math.RoundingMode
import java.util.stream.Collectors


class ProductDataCreator {

    Gson gson = new Gson()


    ProductAveragePriceData productAveragePrice =
            new ProductAveragePriceData(id: 9L, averagePrice: new BigDecimal( 14.99).setScale(2, RoundingMode.HALF_UP))
    UpdateProductsAveragePriceRequest updateRequest = new UpdateProductsAveragePriceRequest(data: [productAveragePrice])

    String updateProductsPricesJson = gson.toJson(updateRequest)


    static Page<ProductIdProjection> createProductIdProjectionPage(List<Long> ids, Pageable pageable) {
        List<ProductIdProjection> projections = ids.stream()
                .map(id -> (ProductIdProjection) () -> id)
                .collect(Collectors.toList());

        return new PageImpl<>(projections, pageable, ids.size());
    }

    String randomProductIdsJson = gson.toJson(createProductIdProjectionPage([1L, 6L, 7L, 9L, 10L], Pageable.ofSize(5)))

}
