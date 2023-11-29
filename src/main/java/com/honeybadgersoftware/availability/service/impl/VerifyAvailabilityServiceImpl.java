package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.model.dto.AvailabilityData;
import com.honeybadgersoftware.availability.model.dto.ProductAvailabilityData;
import com.honeybadgersoftware.availability.model.dto.ProductAvailabilityPerShopData;
import com.honeybadgersoftware.availability.model.dto.ProductPriceData;
import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.model.response.ProductAvailabilityResponse;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.VerifyService;
import com.honeybadgersoftware.availability.component.sorter.SorterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VerifyAvailabilityServiceImpl implements VerifyService {

    private final AvailabilityRepository availabilityRepository;
    private final SorterFactory sorterFactory;

    @Override
    public ProductAvailabilityResponse verifyAvailabilityForAllShops(List<Long> baseProductIds) {
        return prepareAvailabilityResponse(
                verifyProductsAvailability(availabilityRepository.findAllByProductIds(baseProductIds), baseProductIds));
    }

    @Override
    public ProductAvailabilityResponse verifyAvailabilityForSpecificShops(List<Long> productIds, List<Long> shopIds) {
        return prepareAvailabilityResponse(verifyProductsAvailability(
                availabilityRepository.findAllByProductIdsAndShopIds(productIds, shopIds), productIds));
    }

    private ProductAvailabilityResponse prepareAvailabilityResponse(ProductAvailabilityData productAvailabilityData) {

        List<ProductAvailabilityPerShopData> responseData = new ArrayList<>();



        for (Map.Entry<Long, AvailabilityData> entry : productAvailabilityData.getData().entrySet()) {
            List<ProductPriceData> productsPrices = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            for (AvailabilityEntity entity : entry.getValue().getAvailableProducts()) {
                totalPrice = totalPrice.add(entity.getPrice());
                productsPrices.add(ProductPriceData.builder()
                        .productId(entity.getProductId())
                        .price(entity.getPrice())
                        .build());
            }

            responseData.add(ProductAvailabilityPerShopData.builder()
                    .shopId(entry.getKey())
                    .totalPriceOfProducts(totalPrice)
                    .productsPrices(productsPrices)
                    .missingProducts(entry.getValue().getMissingProductsIds())
                    .build());
        }

        return ProductAvailabilityResponse.builder()
                .data(responseData)
                .build();
    }

    private ProductAvailabilityData verifyProductsAvailability(
            List<AvailabilityEntity> availableProducts, List<Long> productIds) {
        Map<Long, List<AvailabilityEntity>> productsByShop =
                sorterFactory.sort(availableProducts, AvailabilityEntity::getShopId);

        ProductAvailabilityData productAvailabilityData = new ProductAvailabilityData(new HashMap<>());

        for (Map.Entry<Long, List<AvailabilityEntity>> entry : productsByShop.entrySet()) {
            if (new HashSet<>(extractProductsIds(entry)).containsAll(productIds)) {
                updateProductAvailabilityData(entry, productAvailabilityData);
            }
        }

        if (productAvailabilityData.getData().isEmpty()) {
            return handleNotFullLists(productsByShop, productIds);
        }

        return productAvailabilityData;
    }

    public void updateProductAvailabilityData(
            Map.Entry<Long, List<AvailabilityEntity>> entry,
            ProductAvailabilityData productAvailabilityData) {

        productAvailabilityData.getData().put(entry.getKey(),
                AvailabilityData.builder()
                        .availableProducts(entry.getValue())
                        .missingProductsIds(Collections.emptyList())
                        .build());
    }

    private ProductAvailabilityData handleNotFullLists(Map<Long, List<AvailabilityEntity>> productsByShop, List<Long> baseProductIds) {
        Optional<Map.Entry<Long, List<AvailabilityEntity>>> maxEntry = findListWithMostRecords(productsByShop);
        ProductAvailabilityData productAvailabilityData = new ProductAvailabilityData(new HashMap<>());
        maxEntry.ifPresent(longListEntry -> processMaxEntry(longListEntry, baseProductIds, productAvailabilityData));
        return productAvailabilityData;
    }

    public void processMaxEntry(
            Map.Entry<Long, List<AvailabilityEntity>> maxEntry,
            List<Long> baseProductIds,
            ProductAvailabilityData productAvailabilityData) {

        productAvailabilityData.getData().put(maxEntry.getKey(), AvailabilityData.builder()
                .availableProducts(maxEntry.getValue())
                .missingProductsIds(filterMissingProducts(baseProductIds, extractProductsIds(maxEntry)))
                .build());
    }

    private Optional<Map.Entry<Long, List<AvailabilityEntity>>> findListWithMostRecords(
            Map<Long, List<AvailabilityEntity>> productsByShop) {
        return productsByShop.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()));
    }

    //jak nie działa to zamień kolejność list
    private List<Long> filterMissingProducts(List<Long> productIdsFromRequest, List<Long> productsInShop) {
        return productIdsFromRequest.stream()
                .filter(id -> !productsInShop.contains(id))
                .toList();
    }

    private List<Long> extractProductsIds(Map.Entry<Long, List<AvailabilityEntity>> productMap) {
        return productMap.getValue().stream()
                .map(AvailabilityEntity::getProductId)
                .toList();
    }
}
