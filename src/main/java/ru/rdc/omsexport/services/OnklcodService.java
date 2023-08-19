package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Onklcod;
import ru.rdc.omsexport.repository.OnklcodRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OnklcodService {
    private final OnklcodRepository repository;

    @Autowired
    public OnklcodService(OnklcodRepository repository) {
        this.repository = repository;
    }

    public Onklcod findOnklcodByLcod(String lcod) {
        return repository.findOnklcodByLcod(lcod);
    }

    public List<Onklcod> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(Onklcod item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Onklcod> list) {
        repository.saveAll(list);
    }
}
