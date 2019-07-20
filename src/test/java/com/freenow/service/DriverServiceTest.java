package com.freenow.service;

import com.freenow.TestData;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DefaultDriverService;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DriverServiceTest extends TestData {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DefaultDriverService driverService;


    @BeforeClass
    public static void setUp() {
        MockitoAnnotations.initMocks(DefaultDriverService.class);
    }


    @Test
    public void findShouldThrowExceptionWhenCannotFindDriverId() {
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                driverService.find(1L));
    }

    @Test
    public void deleteShouldThrowExceptionWhenCannotFindDriverById() {
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                driverService.delete(1L));
    }

    @Test
    public void updateLocationShouldThrowExceptionWhenCannotFindDriverById() {
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                driverService.updateLocation(1L, 12, 12));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenDriverIsOffline() {
        DriverDO driver = getDriver();
        CarDO car = getCar();
        driver.setOnlineStatus(OnlineStatus.OFFLINE);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driver));
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(car));
        Assertions.assertThatExceptionOfType(ConstraintsViolationException.class).isThrownBy(() ->
                driverService.selectCarByDriver(1L, 1L));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenCarIsAlreadyInUse() {
        DriverDO driver = getDriver();
        CarDO car = getCar();
        car.setCarSelectedByDriver(true);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driver));
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(car));
        Assertions.assertThatExceptionOfType(CarAlreadyInUseException.class).isThrownBy(() ->
                driverService.selectCarByDriver(1L, 1L));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenDriverAlreadyHasACar() {
        DriverDO driver = getDriver();
        CarDO car = getCar();
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driver));
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(car));
        Assertions.assertThatExceptionOfType(ConstraintsViolationException.class).isThrownBy(() ->
                driverService.selectCarByDriver(1L, 1L));
    }

    @Test
    public void deselectingACarByDriverShouldThrowExceptionWhenDriverHasNoCar() {
        DriverDO driver = getDriver();
        driver.setCarDO(null);
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driver));
        Assertions.assertThatExceptionOfType(ConstraintsViolationException.class).isThrownBy(() ->
                driverService.deSelectCarByDriver(1L));
    }

    @Test
    public void verifyFindByIdIsCalledForFind() {
        DriverDO driver = getDriver();
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driver));
        driverService.find(any(Long.class));
        verify(driverRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void verifySaveIsCalledForCreate() {
        DriverDO driverDO = getDriver();
        when(driverRepository.save(any(DriverDO.class))).thenReturn(driverDO);
        driverService.create(driverDO);
        verify(driverRepository, times(1)).save(driverDO);
    }


    @Test
    public void verifyFindByIdIsCalledForDelete() {
        DriverDO driverDO = getDriver();
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driverDO));
        driverService.delete(any(Long.class));
        verify(driverRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void verifyFindByIdIsCalledForUpdateLocation() {
        DriverDO driverDO = getDriver();
        when(driverRepository.findById(any(Long.class))).thenReturn(Optional.of(driverDO));
        driverService.updateLocation(1L, 90.0, 90.0);
        verify(driverRepository, times(1)).findById(any(Long.class));
    }


    @Test
    public void verifyFindByOnlineStatusIsCalledForFindByOnlineStatus() {
        List<DriverDO> drivers = Collections.singletonList(getDriver());
        when(driverRepository.findByOnlineStatus(any(OnlineStatus.class))).thenReturn(drivers);
        driverService.find(OnlineStatus.ONLINE);
        verify(driverRepository, times(1)).findByOnlineStatus(any(OnlineStatus.class));
    }

}
