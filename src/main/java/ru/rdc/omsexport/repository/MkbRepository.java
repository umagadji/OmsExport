package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Mkb;

import java.util.Optional;

@Repository
public interface MkbRepository extends JpaRepository<Mkb, Integer> {
    Mkb findMkbByLcod(String lcod);

    Optional<Mkb> findByLcod(String lcod);
}
