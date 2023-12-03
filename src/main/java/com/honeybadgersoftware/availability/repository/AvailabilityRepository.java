package com.honeybadgersoftware.availability.repository;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.model.entity.ProductIdProjection;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {

    @Query("SELECT a FROM AvailabilityEntity a WHERE a.productId IN :productIds")
    List<AvailabilityEntity> findAllByProductIds(@Param("productIds") Collection<Long> productIds);

    @Query("SELECT a FROM AvailabilityEntity a WHERE a.productId IN :productIds AND a.shopId = :shopId")
    List<AvailabilityEntity> findAllByProductIdAndShopId(
            @Param("productIds") Collection<Long> productIds, @Param("shopId") Long shopId);


    @Query("SELECT a FROM AvailabilityEntity a WHERE (a.productId IN :productIds AND a.shopId IN :shopIds)")
    List<AvailabilityEntity> findAllByProductIdsAndShopIds(
            @Param("productIds") Collection<Long> productIds, @Param("shopIds") Collection<Long> shopIds);

    @Query("SELECT e.productId AS id FROM AvailabilityEntity e WHERE e.shopId IN : shopIds")
    Page<ProductIdProjection> findRandomProductsIdsByShopIds(@Param("shopIds") List<Long> shopIds, Pageable pageable);

    @Override
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    long count();
}
