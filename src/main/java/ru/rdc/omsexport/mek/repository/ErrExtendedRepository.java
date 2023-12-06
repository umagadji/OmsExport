package ru.rdc.omsexport.mek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.mek.models.ErrExtended;

@Service
public interface ErrExtendedRepository extends JpaRepository<ErrExtended, Integer> {
}
