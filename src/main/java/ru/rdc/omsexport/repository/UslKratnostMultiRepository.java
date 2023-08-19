package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.UslKratnostMulti;

@Repository
public interface UslKratnostMultiRepository extends JpaRepository<UslKratnostMulti, Integer> {
    UslKratnostMulti getUslKratnostMultiByKsg(String ksg);
}
