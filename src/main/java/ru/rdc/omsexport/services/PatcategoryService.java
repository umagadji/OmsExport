package ru.rdc.omsexport.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Onklcod;
import ru.rdc.omsexport.local_db_models.PatCategory;
import ru.rdc.omsexport.repository.PatcategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PatcategoryService {
    private final PatcategoryRepository repository;

    public PatcategoryService(PatcategoryRepository repository) {
        this.repository = repository;
    }

    public Optional<PatCategory> findByNpolis(String npolis) {
        return repository.findByNpolis(npolis);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<PatCategory> list) {
        repository.saveAll(list);
    }

}
