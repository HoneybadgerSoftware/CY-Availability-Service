package com.honeybadgersoftware.availability.repository;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
