package com.freenow.service;

import com.freenow.TestBase;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.DefaultCarService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarServiceTest extends TestBase {

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
        CarDO carDO = getCarDO();
        when(carRepository.findById(1L)).thenReturn(Optional.of(carDO));
        carService.find(1L);
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    public void verifySaveIsCalledForCreate() {
        CarDO carDO = getCarDO();
        when(carRepository.save(any(CarDO.class))).thenReturn(carDO);
        carService.create(carDO);
        verify(carRepository, times(1)).save(carDO);
    }

    @Test
    public void verifyFindByIdIsCalledForUpdate() {
        CarDO carDO = getCarDO();
        when(carRepository.findById(2L)).thenReturn(Optional.of(carDO));
        carService.updateRating(2L, Rating.FIVE);
        verify(carRepository, times(1)).findById(2L);
    }

    @Test
    public void verifyFindByIdIsCalledForDelete() {
        CarDO carDO = getCarDO();
        when(carRepository.findById(3L)).thenReturn(Optional.of(carDO));
        carService.delete(3L);
        verify(carRepository, times(1)).findById(3L);
    }

    @Test
    public void findShouldThrowExceptionWhenIdDoesNotExist() {
        when(carRepository.findById(4L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> carService.find(4L));
    }

    @Test
    public void deleteShouldShouldThrowExceptionWhenIdDoesNotExist(){
        when(carRepository.findById(5L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> carService.delete(5L));
    }

    @Test
    public void updateShouldShouldThrowExceptionWhenIdDoesNotExist(){
        when(carRepository.findById(6L)).thenReturn(Optional.empty());
        assertExceptionThrown(EntityNotFoundException.class, () -> carService.updateRating(6L, Rating.FIVE));
    }

}
