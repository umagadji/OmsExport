package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.MkbExtended;

import java.util.Optional;

@Repository
public interface MkbExtendedRepository extends JpaRepository<MkbExtended, Integer> {
    Optional<MkbExtended> findByLcod(String lcod);
}
