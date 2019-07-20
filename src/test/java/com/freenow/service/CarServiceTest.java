package com.freenow.service;

import com.freenow.TestData;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.DefaultCarService;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarServiceTest extends TestData {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DefaultCarService carService;

    @BeforeClass
    public static void setUp() {
        MockitoAnnotations.initMocks(DefaultCarService.class);
    }


    @Test
    public void verifyFindByIdIsCalledForFind() {
        CarDO carDO = getCar();
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(carDO));
        carService.find(any(Long.class));
        verify(carRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void verifyFindByManufacturerIsCalledForFindByManufacturer() {
        List<CarDO> cars = Arrays.asList(getCar());
        when(carRepository.findByManufacturer(Manufacturer.MERCEDES)).thenReturn(cars);
        carService.findByManufacturer(Manufacturer.MERCEDES);
        verify(carRepository, times(1)).findByManufacturer(Manufacturer.MERCEDES);
    }

    @Test
    public void verifySaveIsCalledForCreate() {
        CarDO carDO = getCar();
        when(carRepository.save(any(CarDO.class))).thenReturn(carDO);
        carService.create(carDO);
        verify(carRepository, times(1)).save(carDO);
    }

    @Test
    public void verifyFindByIdIsCalledForUpdate() throws Exception {
        CarDO carDO = getCar();
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(carDO));
        carService.updateRating(any(Long.class), Rating.FIVE);
        verify(carRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void verifyFindByIdIsCalledForDelete() {
        CarDO carDO = getCar();
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.of(carDO));
        carService.delete(1L);
        verify(carRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void findThrowExceptionWhenIdDoesNotExist() {
        when(carRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                carService.find(1L));
    }

}
