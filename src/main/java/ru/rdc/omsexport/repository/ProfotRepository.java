package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Profot;

@Repository
public interface ProfotRepository extends JpaRepository<Profot, Integer> {
    Profot findByIdpr(int idprofil);
}
