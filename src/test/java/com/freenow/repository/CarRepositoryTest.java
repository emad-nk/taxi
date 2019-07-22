package com.freenow.repository;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * This test class is based on the data inserted in the DB by data.sql file under resources
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FreeNowServerApplicantTestApplication.class)
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void getCarById() {
        Optional<CarDO> car = carRepository.findById(2L);
        Assertions.assertThat(car.isPresent()).isTrue();
        Assertions.assertThat(car.get().getId()).isEqualTo(2);
    }

    @Test
    public void getAllCars() {
        List<CarDO> cars = Lists.newArrayList(carRepository.findAll());
        Assertions.assertThat(cars).hasSize(4);
    }

    @Test
    public void getCarByIDDoesNotExist(){
        Optional<CarDO> car = carRepository.findById(222L);
        Assertions.assertThat(car.isPresent()).isFalse();
    }

}
