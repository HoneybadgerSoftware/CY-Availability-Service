package com.honeybadgersoftware.availability.service.impl;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.repository.AvailabilityRepository;
import com.honeybadgersoftware.availability.service.VerifyService;
import com.honeybadgersoftware.availability.sorter.SorterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {

    private final AvailabilityRepository availabilityRepository;
    private final SorterFactory sorterFactory;

    @Override
    public void verifyAvailability(List<Long> baseProductIds) {
        List<AvailabilityEntity> availableProducts = availabilityRepository.findAllByProductIds(baseProductIds);

        Map<Long, List<AvailabilityEntity>> productsByShop =
                sorterFactory.sort(availableProducts, AvailabilityEntity::getShopId);

        List<Long> shopsWithAllProducts = new ArrayList<>();
        Map<Long, BigDecimal> totalPricesByShop = new HashMap<>();

        for (Map.Entry<Long, List<AvailabilityEntity>> entry : productsByShop.entrySet()) {
            if (new HashSet<>(extractProductsIds(entry)).containsAll(baseProductIds)) {
                Long entryKey = entry.getKey();
                shopsWithAllProducts.add(entryKey);
                totalPricesByShop.put(entryKey, sumProductsPrices(entry));
            }
        }

        if (shopsWithAllProducts.isEmpty()) {
            handleNotFullLists(productsByShop, baseProductIds);
        } else {
            System.out.println("Sklepy z pełną listą produktów: " + shopsWithAllProducts);
            System.out.println("Sumaryczne ceny w sklepach: " + totalPricesByShop);
        }
    }

    private void handleNotFullLists(Map<Long, List<AvailabilityEntity>> productsByShop, List<Long> baseProductIds) {
        Optional<Map.Entry<Long, List<AvailabilityEntity>>> maxEntry = findListWithMostRecords(productsByShop);
        if (maxEntry.isPresent()) {
            Map.Entry<Long, List<AvailabilityEntity>> entryList = maxEntry.get();
            List<Long> missingProducts = filterMissingProducts(baseProductIds, extractProductsIds(entryList));

            System.out.println("Największa dostępna lista w sklepie ID: " + entryList.getKey());
            System.out.println("Brakujące produkty: " + missingProducts);
        }
    }

    private BigDecimal sumProductsPrices(Map.Entry<Long, List<AvailabilityEntity>> productsByShop) {
        return productsByShop.getValue().stream()
                .map(AvailabilityEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
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
