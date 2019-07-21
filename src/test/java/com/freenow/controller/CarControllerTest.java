package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.TestHelper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CarControllerTest extends TestHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URI = "/v1/cars";
    private static final String MANUFACTURER = "manufacturer";
    private static final String RATING = "rating";
    private MockMvc mvc;

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @BeforeClass
    public static void setUp() {
        MockitoAnnotations.initMocks(CarController.class);
    }

    @Before
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(carController).dispatchOptions(true).build();
    }

    @Test
    public void getCarWithId() throws Exception {
        CarDO carDO = getCar();
        doReturn(carDO).when(carService).find(anyLong());
        carController.getCar(1L);
        MvcResult result = mvc
                .perform(get(BASE_URI + "/{carId}", 1L))
                .andReturn();
        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        final String responseBody = result.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).contains("GMB12");
    }

    @Test
    public void getAllCarsByManufacturer() throws Exception {
        List<CarDO> cars = Collections.singletonList(getCar());
        doReturn(cars).when(carService).findByManufacturer(any(Manufacturer.class));
        carController.findCars(Manufacturer.MERCEDES);
        MvcResult result = mvc
                .perform(get(BASE_URI)
                        .param(MANUFACTURER, Manufacturer.MERCEDES.toString()))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
        Assertions.assertThat(result.getResponse().getContentAsString()).contains("GMB12");
    }

    @Test
    public void createCar() throws Exception {
        CarDO carDO = getCar();
        CarDTO carDTO = getCarDTO();
        String jsonInString = mapper.writeValueAsString(carDTO);
        doReturn(carDO).when(carService).create(any(CarDO.class));
        carController.createCar(carDTO);
        MvcResult result = mvc
                .perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(jsonInString))
                .andReturn();
        assertHttpStatus(result, HttpStatus.CREATED);
        Assertions.assertThat(result.getResponse().getContentAsString()).contains("FOUR");
    }

    @Test
    public void updateCar() throws Exception {
        CarDTO carDTO = getCarDTO();
        String jsonInString = mapper.writeValueAsString(carDTO);
        doNothing().when(carService).updateRating(anyLong(), any(Rating.class));
        carController.updateRating(1L, Rating.FIVE);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/{carId}", 1L)
                        .param(RATING, Rating.FIVE.toString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(jsonInString))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteCar() throws Exception {
        doNothing().when(carService).delete(anyLong());
        carController.deleteCar(1L);
        MvcResult result = mvc
                .perform(delete(BASE_URI + "/{carId}", 1L))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void getCarWithNonExistingIdShouldThrowException() throws Exception {
        when(carService.find(anyLong())).thenThrow(new EntityNotFoundException("Entity not found"));
        assertExceptionThrown(EntityNotFoundException.class, () -> carController.getCar(1L));
        mvc.perform(get(BASE_URI + "/{carId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void deleteNonExistingCarShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(carService).delete(anyLong());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                carController.deleteCar(1L));
        mvc.perform(delete(BASE_URI + "/{carId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void updateRatingForNonExistingCarShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(carService).updateRating(anyLong(), any());
        assertExceptionThrown(EntityNotFoundException.class, () -> carController.updateRating(1L, Rating.FIVE));
        mvc.perform(put(BASE_URI + "/{carId}", 1L)
                .param(RATING, Rating.FIVE.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void createCarShouldThrowExceptionWhenCarCannotBeSaved() throws Exception {
        CarDTO carDTO = getCarDTO();
        String jsonInString = mapper.writeValueAsString(carDTO);
        when(carService.create(any())).thenThrow(new ConstraintsViolationException("Failed"));
        assertExceptionThrown(ConstraintsViolationException.class, () -> carController.createCar(getCarDTO()));
        mvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonInString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

}
