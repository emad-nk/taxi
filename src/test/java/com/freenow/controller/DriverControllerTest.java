package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.TestBase;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.DriverDO;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DriverService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class DriverControllerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String BASE_URI = "/v1/drivers";
    private static final String DRIVER_ID = "driverId";
    private static final String CAR_ID = "carId";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private MockMvc mvc;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController driverController;

    @Before
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(driverController).dispatchOptions(true).build();
    }


    @Test
    public void getDriverById() throws Exception {
        DriverDO driverDO = getDriverDO();
        doReturn(driverDO).when(driverService).find(1L);
        MvcResult result = mvc
                .perform(get(BASE_URI + "/{driverId}", 1))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.password").value("pass1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
    }

    @Test
    public void updateLocation() throws Exception {
        doReturn(getDriverDO()).when(driverService).updateLocation(1, 33, 33);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/{driverId}", 1)
                        .param(LONGITUDE, "33").param(LATITUDE, "33"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
    }

    @Test
    public void createDriver() throws Exception {
        DriverDO driverDO = getDriverDO();
        DriverDTO driverDTO = getDriverDTO();
        String jsonInString = MAPPER.writeValueAsString(driverDTO);
        doReturn(driverDO).when(driverService).create(any(DriverDO.class));
        MvcResult result = mvc
                .perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonInString))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.password").value("pass1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.CREATED);
    }

    @Test
    public void deleteDriver() throws Exception {
        doNothing().when(driverService).delete(1L);
        driverController.deleteDriver(1L);
        MvcResult result = mvc
                .perform(delete(BASE_URI + "/{driverId}", 1))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void selectCarByDriver() throws Exception {
        doReturn(getDriverDO()).when(driverService).selectCarByDriver(1L, 1L);
        driverController.selectCarByDriver(1L, 1L);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/select")
                        .param(DRIVER_ID, "1")
                        .param(CAR_ID, "1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
    }

    @Test
    public void deSelectCarByDriver() throws Exception {
        doReturn(getDriverDO()).when(driverService).deSelectCarByDriver(1L);
        driverController.deSelectCarByDriver(1L);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/deselect")
                        .param(DRIVER_ID, "1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
    }

    @Test
    public void getDriverWithNonExistingIdShouldThrowException() throws Exception {
        when(driverService.find(1L)).thenThrow(new EntityNotFoundException("Entity not found"));
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.getDriver(1L));
        mvc.perform(get(BASE_URI + "/{driverId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void deleteNonExistingDriverShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(driverService).delete(1L);
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.deleteDriver(1L));
        mvc.perform(delete(BASE_URI + "/{driverId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void updateLocationForNonExistingDriverShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found"))
                .when(driverService).updateLocation(1L, 2, 2);
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.updateLocation(1L, 2, 2));
        mvc.perform(put(BASE_URI + "/{driverId}", 1L)
                .param(LONGITUDE, "2")
                .param(LATITUDE, "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void createDriverShouldThrowExceptionWhenDriverCannotBeSaved() throws Exception {
        DriverDTO driverDTO = getDriverDTO();
        String jsonInString = MAPPER.writeValueAsString(driverDTO);
        when(driverService.create(any())).thenThrow(new ConstraintsViolationException("Failed"));
        assertExceptionThrown(ConstraintsViolationException.class, () -> driverController.createDriver(getDriverDTO()));
        mvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonInString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void selectACarInUseShouldThrowCarAlreadyInUseException() throws Exception {
        doThrow(CarAlreadyInUseException.class).when(driverService).selectCarByDriver(1L, 1L);
        assertExceptionThrown(CarAlreadyInUseException.class, () -> driverController.selectCarByDriver(1L, 1L));
        mvc.perform(put(BASE_URI + "/select")
                .param(DRIVER_ID, "1")
                .param(CAR_ID, "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void testFindDriverByCarAttributes() throws Exception {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"username", "user1"},
                {"carseat", "4"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        doReturn(Collections.singletonList(getDriverDO())).when(driverService).searchDrivers(queryMap);
        mvc.perform(get(BASE_URI + "/search")
                .param("username", "user1")
                .param("carseat", "4"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

}
