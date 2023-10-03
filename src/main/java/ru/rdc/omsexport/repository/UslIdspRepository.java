package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.UslIdsp;

@Repository
public interface UslIdspRepository extends JpaRepository<UslIdsp, Long> {
    UslIdsp getUslIdspByKsg(String code_usl);
}
