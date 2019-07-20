package com.freenow;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.class)
public abstract class TestData {

    public CarDO getCar() {
        CarDO car = new CarDO();
        car.setId(1L);
        car.setSeatCount((short) 5);
        car.setRating(Rating.FOUR);
        car.setDateCarCreated(ZonedDateTime.now());
        car.setLicensePlate("GMB12");
        car.setEngineType(EngineType.DIESEL);
        car.setConvertible(true);
        car.setManufacturer(Manufacturer.MERCEDES);
        return car;
    }


    public CarDTO getCarDTO() {
        return CarDTO.newBuilder()
                .setConvertible(true)
                .setEngineType(EngineType.DIESEL)
                .setLicensePlate("GMB12")
                .setSeatCount((short) 5)
                .setManufacturer(Manufacturer.MERCEDES)
                .setRating(Rating.FOUR)
                .createCarDTO();
    }


    public DriverDO getDriver() {
        DriverDO driver = new DriverDO("user1", "pass1");
        driver.setId(1L);
        driver.setCarDO(getCar());
        driver.setDateCreated(ZonedDateTime.now());
        driver.setDeleted(false);
        driver.setOnlineStatus(OnlineStatus.ONLINE);
        GeoCoordinate geoCoordinate = new GeoCoordinate(80, 40);
        driver.setCoordinate(geoCoordinate);
        return driver;
    }


    public DriverDTO getDriverDTO() {
        GeoCoordinate geoCoordinate = new GeoCoordinate(80, 40);
        return DriverDTO.newBuilder()
                .setId(1L)
                .setUsername("user1")
                .setPassword("pass1")
                .setCarDTO(getCarDTO())
                .setCoordinate(geoCoordinate)
                .createDriverDTO();
    }

}
