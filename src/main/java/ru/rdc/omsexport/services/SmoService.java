package ru.rdc.omsexport.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Smo;
import ru.rdc.omsexport.repository.SmoRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SmoService {
    private final SmoRepository repository;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    public SmoService(SmoRepository repository) {
        this.repository = repository;
    }

    public Optional<Smo> findBySmocod(String smocod) {
        return repository.findBySmocod(smocod);
    }

    @Transactional
    public void save(Smo item) {
        repository.save(item);
    }

    @Transactional
    public void saveAll(List<Smo> list) {
        repository.saveAll(list);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

}
