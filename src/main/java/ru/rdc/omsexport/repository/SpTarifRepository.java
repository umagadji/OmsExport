package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.SpTarif;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpTarifRepository extends JpaRepository<SpTarif, Integer> {
    List<SpTarif> findAllByKsg(String ksg);

    Optional<SpTarif> findByKsg(String ksg);
}
