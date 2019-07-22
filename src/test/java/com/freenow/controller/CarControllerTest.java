package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.TestBase;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CarControllerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String BASE_URI = "/v1/cars";
    private static final String RATING = "rating";
    private MockMvc mvc;

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @Before
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(carController).dispatchOptions(true).build();
    }

    @Test
    public void getCarWithId() throws Exception {
        CarDO carDO = getCarDO();
        doReturn(carDO).when(carService).find(1L);
        MvcResult result = mvc
                .perform(get(BASE_URI + "/{carId}", 1L))
                .andReturn();
        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        CarDTO resultObject = getMappedObjectDTO(result, CarDTO.class);

        Assertions.assertThat(resultObject.getRating()).isEqualTo(Rating.FIVE);
        Assertions.assertThat(resultObject.getLicensePlate()).isEqualTo("GMB12");
        Assertions.assertThat(resultObject.getEngineType()).isEqualTo(EngineType.DIESEL);
    }

    @Test
    public void createCar() throws Exception {
        CarDO carDO = getCarDO();
        CarDTO carDTO = getCarDTO();
        String jsonInString = MAPPER.writeValueAsString(carDTO);
        doReturn(carDO).when(carService).create(any(CarDO.class));
        MvcResult result = mvc
                .perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(jsonInString))
                .andReturn();
        assertHttpStatus(result, HttpStatus.CREATED);
        CarDTO resultObject = getMappedObjectDTO(result, CarDTO.class);

        Assertions.assertThat(resultObject.getRating()).isEqualTo(Rating.FIVE);
        Assertions.assertThat(resultObject.getLicensePlate()).isEqualTo("GMB12");
        Assertions.assertThat(resultObject.getEngineType()).isEqualTo(EngineType.DIESEL);
    }

    @Test
    public void updateCar() throws Exception {
        CarDTO carDTO = getCarDTO();
        CarDO carDO = getCarDO();
        String jsonInString = MAPPER.writeValueAsString(carDTO);
        doReturn(carDO).when(carService).updateRating(1L, Rating.FIVE);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/{carId}", 1L)
                        .param(RATING, Rating.FIVE.toString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(jsonInString))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);

        CarDTO resultObject = getMappedObjectDTO(result, CarDTO.class);

        Assertions.assertThat(resultObject.getRating()).isEqualTo(Rating.FIVE);
        Assertions.assertThat(resultObject.getLicensePlate()).isEqualTo("GMB12");
        Assertions.assertThat(resultObject.getEngineType()).isEqualTo(EngineType.DIESEL);
    }

    @Test
    public void deleteCar() throws Exception {
        doNothing().when(carService).delete(1L);
        MvcResult result = mvc
                .perform(delete(BASE_URI + "/{carId}", 1L))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void getCarWithNonExistingIdShouldThrowException() throws Exception {
        when(carService.find(12L)).thenThrow(new EntityNotFoundException("Entity not found"));
        assertExceptionThrown(EntityNotFoundException.class, () -> carController.getCar(12L));
        mvc.perform(get(BASE_URI + "/{carId}", 12L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void deleteNonExistingCarShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(carService).delete(anyLong());
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->
                carController.deleteCar(15L));
        mvc.perform(delete(BASE_URI + "/{carId}", 15L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void updateRatingForNonExistingCarShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(carService).updateRating(anyLong(), any());
        assertExceptionThrown(EntityNotFoundException.class, () -> carController.updateRating(17L, Rating.FIVE));
        mvc.perform(put(BASE_URI + "/{carId}", 17L)
                .param(RATING, Rating.FIVE.toString()))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void createCarShouldThrowExceptionWhenCarCannotBeSaved() throws Exception {
        CarDTO carDTO = getCarDTO();
        String jsonInString = MAPPER.writeValueAsString(carDTO);
        when(carService.create(any())).thenThrow(new ConstraintsViolationException("Failed"));
        assertExceptionThrown(ConstraintsViolationException.class, () -> carController.createCar(getCarDTO()));
        mvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonInString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

}
