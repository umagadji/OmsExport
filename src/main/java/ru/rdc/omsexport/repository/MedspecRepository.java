package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Medspec;

import java.util.Optional;

@Repository
public interface MedspecRepository extends JpaRepository<Medspec, Integer> {
    Optional<Medspec> findByIdmsp(String idmsp);
}
