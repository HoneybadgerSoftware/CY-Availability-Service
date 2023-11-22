package com.honeybadgersoftware.availability.repository;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {
}
