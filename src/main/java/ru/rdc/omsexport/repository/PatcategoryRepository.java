package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.PatCategory;

import java.util.Optional;

@Repository
public interface PatcategoryRepository extends JpaRepository<PatCategory, Long> {
        Optional<PatCategory> findByNpolis(String npolis);
}