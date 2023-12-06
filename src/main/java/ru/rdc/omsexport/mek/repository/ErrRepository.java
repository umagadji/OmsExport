package ru.rdc.omsexport.mek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.mek.models.Err;

@Repository
public interface ErrRepository extends JpaRepository<Err, Integer> {
}
