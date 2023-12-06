package ru.rdc.omsexport.mek.utils;

import org.springframework.stereotype.Service;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.mek.models.Err;
import ru.rdc.omsexport.mek.models.ErrExtended;
import ru.rdc.omsexport.mek.models.Plan;
import ru.rdc.omsexport.mek.service.ErrExtendedService;
import ru.rdc.omsexport.mek.service.ErrService;
import ru.rdc.omsexport.mek.service.PlanService;

import java.util.List;

@Service
//Сервис, который позволяет работать с таблицами err, plan, err_extended и другими будущими
public class CreateReportService {
    private final ErrService errService;
    private final PlanService planService;
    private final ErrExtendedService errExtendedService;

    public CreateReportService(ErrService errService, PlanService planService, ErrExtendedService errExtendedService) {
        this.errService = errService;
        this.planService = planService;
        this.errExtendedService = errExtendedService;
    }

    //Читает таблицу с услугами из плана
    public List<Plan> readPlanFiles() {
        return ReadPlanDBF.readPlanDBF(AppConstants.planFilePath + "plan.dbf", "windows-1251");
    }

    //Записывает таблицу с услугами из плана в БД
    public void savePlanTable(List<Plan> planList) {
        planService.saveAll(planList);
    }

    //Записывает в БД таблицу с ошибками из МЭК
    public void saveErrTableinDB(List<Err> errList) {
        errService.saveAll(errList);
    }

    //Сохраняет в БД сводную таблицу errExtended, содержащую поля из err и plan
    public void saveErrExtentedTableInDB() {
        errExtendedService.saveErrExtendedList();
    }

    //Удаляет данные из всех таблиц, относящихся к МЭК и таблицы с услугами из объемов
    public void deleteDataFromAllTable() {
        errService.deleteAll();
        planService.deleteAll();
        errExtendedService.deleteAll();
    }

    //Получает все ошибки из расширенной таблицы МЭК
    public List<ErrExtended> getAllErrExtendedList() {
        return errExtendedService.getAllErrExtendedList();
    }
}
