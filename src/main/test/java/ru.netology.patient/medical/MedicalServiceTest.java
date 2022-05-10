package ru.netology.patient.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceTest {

    private final PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
    private final SendAlertService alertService = Mockito.mock(SendAlertService.class);

    private MedicalService medicalServiceMock() {
        Mockito.when(patientInfoFileRepository.getById(Mockito.any()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        return new MedicalServiceImpl(patientInfoFileRepository, alertService);


    }

    @Test
    public void checkBloodPressureTest() {
        medicalServiceMock().checkBloodPressure("31134", new BloodPressure(200, 100));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
    }
    @Test
    public void bloodPressureIsNormalTest() {
        medicalServiceMock().checkBloodPressure("31134", new BloodPressure(120, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService, Mockito.times(0)).send(argumentCaptor.capture());
    }

    @Test
    public void checkTemperatureTest() {

        medicalServiceMock().checkTemperature("12", new BigDecimal("35"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
    }
    @Test
    public void temperatureIsNormalTest() {

        medicalServiceMock().checkTemperature("12", new BigDecimal("36.65"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService, Mockito.times(0)).send(argumentCaptor.capture());
    }
}
