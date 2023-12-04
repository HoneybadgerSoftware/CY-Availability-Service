package com.honeybadgersoftware.availability.data

import com.google.gson.Gson
import com.honeybadgersoftware.availability.model.dto.ProductAveragePriceData
import com.honeybadgersoftware.availability.model.request.UpdateProductsAveragePriceRequest

import java.math.RoundingMode

class ProductDataCreator {

    Gson gson = new Gson()


    ProductAveragePriceData productAveragePrice =
            new ProductAveragePriceData(id: 9L, averagePrice: new BigDecimal(14.99).setScale(2, RoundingMode.HALF_UP))
    UpdateProductsAveragePriceRequest updateRequest = new UpdateProductsAveragePriceRequest(data: [productAveragePrice])

    String updateProductsPricesJson = gson.toJson(updateRequest)

}
