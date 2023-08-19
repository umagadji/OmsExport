package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Slpu;
import ru.rdc.omsexport.repository.SlpuRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SlpuService {
    private final SlpuRepository repository;

    @Autowired
    public SlpuService(SlpuRepository repository) {
        this.repository = repository;
    }

    public List<Slpu> findSlpuByGlpu(String glpu) {
        return repository.findSlpuByGlpu(glpu);
    }

    public Optional<Slpu> findSlpuByMcod(String mcod) {
        return repository.findSlpuByMcod(mcod);
    }

    public Slpu findSlpuByIdump(int idump) {
        return repository.findSlpuByIdump(idump);
    }

    @Transactional
    public void save(Slpu item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Slpu> list) {
        repository.saveAll(list);
    }

}
