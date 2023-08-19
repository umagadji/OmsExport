package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.SpTarifAdd;

@Repository
public interface SpTarifAddRepository extends JpaRepository<SpTarifAdd, Integer> {
    SpTarifAdd findByKsg(String ksg);
}
