package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.SpTarifExtended;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpTarifExtendedRepository extends JpaRepository<SpTarifExtended, Integer> {
    List<SpTarifExtended> findAllByKsg(String ksg);

    Optional<SpTarifExtended> findByKsg(String ksg);
}
