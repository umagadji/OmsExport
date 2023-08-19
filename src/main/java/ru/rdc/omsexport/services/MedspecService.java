package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Medspec;
import ru.rdc.omsexport.repository.MedspecRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MedspecService {
    private final MedspecRepository repository;

    @Autowired
    public MedspecService(MedspecRepository repository) {
        this.repository = repository;
    }

    public Optional<Medspec> findByIdmsp(String idmsp) {
        return repository.findByIdmsp(idmsp);
    }

    @Transactional
    public void save(Medspec item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Medspec> list) {
        repository.saveAll(list);
    }
}
