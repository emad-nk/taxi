package com.freenow.repository;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * This test class is based on the data inserted in the DB by data.sql file under resources
 */
public class CarRepositoryTest extends RepositoryTestData {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void getDriverById() {
        Optional<CarDO> car = carRepository.findById(2L);
        Assertions.assertThat(car.isPresent()).isTrue();
    }

    @Test
    public void getAllCars() {
        List<CarDO> cars = Lists.newArrayList(carRepository.findAll());
        Assertions.assertThat(cars).hasSize(4);
    }

}