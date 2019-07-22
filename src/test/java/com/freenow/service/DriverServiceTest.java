package com.freenow.service;

import com.freenow.TestBase;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DefaultDriverService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceTest extends TestBase {

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
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> driverService.find(1L));
    }

    @Test
    public void deleteShouldThrowExceptionWhenCannotFindDriverById() {
        when(driverRepository.findById(2L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> driverService.delete(2L));
    }

    @Test
    public void updateLocationShouldThrowExceptionWhenCannotFindDriverById() {
        when(driverRepository.findById(3L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> driverService.updateLocation(3L, 12, 12));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenDriverIsOffline() {
        DriverDO driver = getDriverDO();
        CarDO car = getCarDO();
        driver.setOnlineStatus(OnlineStatus.OFFLINE);
        when(driverRepository.findById(4L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(5L)).thenReturn(Optional.of(car));
        assertExceptionThrown(ConstraintsViolationException.class, () -> driverService.selectCarByDriver(4L, 5L));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenCarIsAlreadyInUse() {
        DriverDO driver = getDriverDO();
        CarDO car = getCarDO();
        car.setCarSelectedByDriver(true);
        when(driverRepository.findById(6L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(7L)).thenReturn(Optional.of(car));
        assertExceptionThrown(CarAlreadyInUseException.class, () -> driverService.selectCarByDriver(6L, 7L));
    }

    @Test
    public void selectCarByDriverShouldThrowExceptionWhenDriverAlreadyHasACar() {
        DriverDO driver = getDriverDO();
        CarDO car = getCarDO();
        when(driverRepository.findById(8L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(9L)).thenReturn(Optional.of(car));
        assertExceptionThrown(ConstraintsViolationException.class, () -> driverService.selectCarByDriver(8L, 9L));
    }

    @Test
    public void deselectingACarByDriverShouldThrowExceptionWhenDriverHasNoCar() {
        DriverDO driver = getDriverDO();
        driver.setCarDO(null);
        when(driverRepository.findById(10L)).thenReturn(Optional.of(driver));
        assertExceptionThrown(ConstraintsViolationException.class, () -> driverService.deSelectCarByDriver(10L));
    }

    @Test
    public void verifyFindByIdIsCalledForFind() {
        DriverDO driver = getDriverDO();
        when(driverRepository.findById(11L)).thenReturn(Optional.of(driver));
        driverService.find(11L);
        verify(driverRepository, times(1)).findById(11L);
    }


    @Test
    public void verifySaveIsCalledForCreate() {
        DriverDO driverDO = getDriverDO();
        when(driverRepository.save(any(DriverDO.class))).thenReturn(driverDO);
        driverService.create(driverDO);
        verify(driverRepository, times(1)).save(driverDO);
    }


    @Test
    public void verifyFindByIdIsCalledForDelete() {
        DriverDO driverDO = getDriverDO();
        when(driverRepository.findById(12L)).thenReturn(Optional.of(driverDO));
        driverService.delete(12L);
        verify(driverRepository, times(1)).findById(12L);
    }


    @Test
    public void verifyFindByIdIsCalledForUpdateLocation() {
        DriverDO driverDO = getDriverDO();
        when(driverRepository.findById(13L)).thenReturn(Optional.of(driverDO));
        driverService.updateLocation(13L, 90.0, 90.0);
        verify(driverRepository, times(1)).findById(13L);
    }

}
