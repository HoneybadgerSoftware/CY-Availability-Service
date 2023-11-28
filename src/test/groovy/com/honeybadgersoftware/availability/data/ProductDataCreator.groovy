package com.honeybadgersoftware.availability.data

import com.google.gson.Gson
import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest

import java.math.RoundingMode


class ProductDataCreator {

    ProductAveragePriceData productAveragePrice = new ProductAveragePriceData(id: 1L, averagePrice: new BigDecimal( 37.49).setScale(2, RoundingMode.HALF_UP))
    UpdateProductsAveragePriceRequest updateRequest = new UpdateProductsAveragePriceRequest(data: [productAveragePrice])

    Gson gson = new Gson()
    String updateProductsPricesJson = gson.toJson(updateRequest)
}
