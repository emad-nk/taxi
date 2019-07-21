package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.TestHelper;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DriverService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class DriverControllerTest extends TestHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
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

    @BeforeClass
    public static void setUp() {
        MockitoAnnotations.initMocks(DriverController.class);
    }

    @Before
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(driverController).dispatchOptions(true).build();
    }


    @Test
    public void getDriverById() throws Exception {
        DriverDO driverDO = getDriver();
        doReturn(driverDO).when(driverService).find(anyLong());
        driverController.getDriver(1L);
        MvcResult result = mvc
                .perform(get(BASE_URI + "/{driverId}", 1))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
        final String responseBody = result.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).contains("user1");
    }

    @Test
    public void updateLocation() throws Exception {
        doNothing().when(driverService).updateLocation(anyLong(), anyDouble(), anyDouble());
        driverController.updateLocation(1L, 99, 99);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/{driverId}", 1)
                        .param(LONGITUDE, "33").param(LATITUDE, "33"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void createDriver() throws Exception {
        DriverDO driverDO = getDriver();
        DriverDTO driverDTO = getDriverDTO();
        String jsonInString = mapper.writeValueAsString(driverDO);
        doReturn(driverDO).when(driverService).create(any(DriverDO.class));
        driverController.createDriver(driverDTO);
        MvcResult result = mvc
                .perform(post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonInString))
                .andReturn();
        assertHttpStatus(result, HttpStatus.CREATED);
        final String responseBody = result.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).contains("user1");
    }

    @Test
    public void deleteDriver() throws Exception {
        doNothing().when(driverService).delete(anyLong());
        driverController.deleteDriver(1L);
        MvcResult result = mvc
                .perform(delete(BASE_URI + "/{driverId}", 1))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void findDriverByOnlineStatus() throws Exception {
        List<DriverDO> drivers = Collections.singletonList(getDriver());
        doReturn(drivers).when(driverService).find(any(OnlineStatus.class));
        driverController.findDrivers(OnlineStatus.ONLINE);
        MvcResult result = mvc
                .perform(get(BASE_URI)
                        .param("onlineStatus", OnlineStatus.ONLINE.toString()))
                .andReturn();
        assertHttpStatus(result, HttpStatus.OK);
        final String responseBody = result.getResponse().getContentAsString();
        Assertions.assertThat(responseBody).contains("user1");
    }

    @Test
    public void selectCarByDriver() throws Exception {
        doNothing().when(driverService).selectCarByDriver(anyLong(), anyLong());
        driverController.selectCarByDriver(1L, 1L);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/select")
                        .param(DRIVER_ID, "1")
                        .param(CAR_ID, "1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void deSelectCarByDriver() throws Exception {
        doNothing().when(driverService).deSelectCarByDriver(anyLong());
        driverController.deSelectCarByDriver(1L);
        MvcResult result = mvc
                .perform(put(BASE_URI + "/deselect")
                        .param(DRIVER_ID, "1"))
                .andReturn();
        assertHttpStatus(result, HttpStatus.NO_CONTENT);
    }

    @Test
    public void getDriverWithNonExistingIdShouldThrowException() throws Exception {
        when(driverService.find(anyLong())).thenThrow(new EntityNotFoundException("Entity not found"));
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.getDriver(1L));
        mvc.perform(get(BASE_URI + "/{driverId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void deleteNonExistingDriverShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(driverService).delete(anyLong());
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.deleteDriver(1L));
        mvc.perform(delete(BASE_URI + "/{driverId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void updateLocationForNonExistingDriverShouldThrowException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found"))
                .when(driverService).updateLocation(anyLong(), anyDouble(), anyDouble());
        assertExceptionThrown(EntityNotFoundException.class, () -> driverController.updateLocation(1L, 2, 2));
        mvc.perform(put(BASE_URI + "/{driverId}", 1L)
                .param(LONGITUDE, "2")
                .param(LATITUDE, "2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void createDriverShouldThrowExceptionWhenDriverCannotBeSaved() throws Exception {
        DriverDTO driverDTO = getDriverDTO();
        String jsonInString = mapper.writeValueAsString(driverDTO);
        when(driverService.create(any())).thenThrow(new ConstraintsViolationException("Failed"));
        assertExceptionThrown(ConstraintsViolationException.class, () -> driverController.createDriver(getDriverDTO()));
        mvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonInString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void selectACarInUseShouldThrowCarAlreadyInUseException() throws Exception {
        doThrow(CarAlreadyInUseException.class).when(driverService).selectCarByDriver(anyLong(), anyLong());
        assertExceptionThrown(CarAlreadyInUseException.class, () -> driverController.selectCarByDriver(1L, 1L));
        mvc.perform(put(BASE_URI + "/select")
                .param(DRIVER_ID, "1")
                .param(CAR_ID, "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void testFindDriverByCarAttributes() throws Exception {

    }

}
