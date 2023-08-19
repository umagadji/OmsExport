package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Smo;

import java.util.Optional;

@Repository
public interface SmoRepository extends JpaRepository<Smo, Integer> {
    Optional<Smo> findBySmocod(String smocod);
}
