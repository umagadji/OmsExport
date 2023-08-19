package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Serwitelist;
import ru.rdc.omsexport.repository.SerwitelistRepository;

import java.util.List;

@Service
public class SerwitelistService {
    private final SerwitelistRepository repository;

    @Autowired
    public SerwitelistService(SerwitelistRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<Serwitelist> list) {
        repository.saveAll(list);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
