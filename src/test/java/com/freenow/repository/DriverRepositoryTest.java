package com.freenow.repository;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.TestBase;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.DriverDO;
import com.freenow.exception.ParseValueException;
import com.freenow.search.SearchCriteria;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This test class is based on the data inserted in the DB by data.sql file under resources
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FreeNowServerApplicantTestApplication.class)
public class DriverRepositoryTest extends TestBase {

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void getDriverById() {
        Optional<DriverDO> driverDO = driverRepository.findById(4L);
        Assertions.assertThat(driverDO.isPresent()).isTrue();
        Assertions.assertThat(driverDO.get().getId()).isEqualTo(4);
    }

    @Test
    public void getDriverDoesNotExistById() {
        Optional<DriverDO> driverDO = driverRepository.findById(222L);
        Assertions.assertThat(driverDO.isPresent()).isFalse();
    }

    @Test
    public void getAllDrivers() {
        List<DriverDO> drivers = Lists.newArrayList(driverRepository.findAll());
        Assertions.assertThat(drivers.size()).isEqualTo(10);
    }


    @Test
    public void getDriversBasedOnStatusOnlineAndUsername() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"onlineStatus", "online"},
                {"username", "driver09"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        List<DriverDO> drivers = driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap));
        Assertions.assertThat(drivers.size()).isEqualTo(1);
    }

    @Test
    public void getDriversBasedOnStatusOnlineAndEngineType() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"onlineStatus", "online"},
                {"enginetype", "gas"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        List<DriverDO> drivers = driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap));
        Assertions.assertThat(drivers.size()).isEqualTo(2);
    }

    @Test
    public void getDriversBasedOnStatusOnlineEngineTypeAndConvertible() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"onlineStatus", "online"},
                {"enginetype", "gas"},
                {"convertible", "true"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        List<DriverDO> drivers = driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap));
        Assertions.assertThat(drivers.size()).isEqualTo(2);
    }

    @Test
    public void getDriversBasedOnStatusOnlineEngineTypeAndConvertibleFalseShouldNotExist() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"onlineStatus", "online"},
                {"enginetype", "gas"},
                {"convertible", "false"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        List<DriverDO> drivers = driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap));
        Assertions.assertThat(drivers.size()).isEqualTo(0);
    }

    @Test
    public void getDriversBasedOnAllCharacteristics() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"onlineStatus", "online"},
                {"enginetype", "gas"},
                {"convertible", "true"},
                {"licenseplate", "GMB124"},
                {"seatcount", "4"},
                {"Manufacturer", "mercedes"},
                {"rating", "five"},
                {"username", "driver09"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        List<DriverDO> drivers = driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap));
        Assertions.assertThat(drivers.size()).isEqualTo(1);
    }

    @Test
    public void getDriversShouldThrowExceptionIfTypeIsNotParsable() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"rating", "fail"},
                {"username", "driver09"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        assertExceptionThrown(ParseValueException.class, () ->
                driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap)));
    }

    @Test
    public void getDriversShouldThrowExceptionIfWrongVariableIsSent() {
        Map<String, String> queryMap = Stream.of(new String[][]{
                {"rating", "five"},
                {"FAIL", "driver09"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        assertExceptionThrown(ParseValueException.class, () ->
                driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryMap)));
    }

}
