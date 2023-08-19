package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Mkb;
import ru.rdc.omsexport.repository.MkbRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MkbService {
    private final MkbRepository repository;

    @Autowired
    public MkbService(MkbRepository repository) {
        this.repository = repository;
    }

    public Mkb findMkbByLcod(String lcod) {
        return repository.findMkbByLcod(lcod);
    }

    @Transactional
    public void save(Mkb item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Mkb> list) {
        repository.saveAll(list);
    }
}
