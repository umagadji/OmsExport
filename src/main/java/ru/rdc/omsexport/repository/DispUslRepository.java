package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.DispUsl;

import java.util.Optional;

@Repository
public interface DispUslRepository extends JpaRepository<DispUsl, Long> {
    Optional<DispUsl> getDispUslByKsg(String code_usl);
}
