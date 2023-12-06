package ru.rdc.omsexport.mek.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.mek.models.Err;
import ru.rdc.omsexport.mek.repository.ErrRepository;

import java.util.List;

@Service
public class ErrService {
    private final ErrRepository repository;

    public ErrService(ErrRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<Err> list) {
        repository.saveAll(list);
    }

    public List<Err> getList() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
