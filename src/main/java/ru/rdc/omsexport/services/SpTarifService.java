package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.SpTarif;
import ru.rdc.omsexport.repository.SpTarifRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SpTarifService {
    private final SpTarifRepository repository;

    @Autowired
    public SpTarifService(SpTarifRepository repository) {
        this.repository = repository;
    }

    public List<SpTarif> findAllByKsg(String ksg) {
        return repository.findAllByKsg(ksg);
    }

    public Optional<SpTarif> findByKsg(String ksg) {
        return repository.findByKsg(ksg);
    }

    @Transactional
    public void save(SpTarif item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<SpTarif> list) {
        repository.saveAll(list);
    }
}
