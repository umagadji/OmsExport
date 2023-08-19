package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.local_db_models.Serwitelist;

@Repository
public interface SerwitelistRepository extends JpaRepository<Serwitelist, Integer> {
}
