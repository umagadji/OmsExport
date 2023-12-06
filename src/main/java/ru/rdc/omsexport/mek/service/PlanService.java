package ru.rdc.omsexport.mek.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.mek.models.Plan;
import ru.rdc.omsexport.mek.repository.PlanRepository;

import java.util.List;

@Service
public class PlanService {
    private final PlanRepository repository;

    public PlanService(PlanRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<Plan> list) {
        repository.saveAll(list);
    }

    public List<Plan> getList() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
