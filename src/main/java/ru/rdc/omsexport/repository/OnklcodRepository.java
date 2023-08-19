package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Onklcod;

@Repository
public interface OnklcodRepository extends JpaRepository<Onklcod, Integer> {
    Onklcod findOnklcodByLcod(String lcod);
}
