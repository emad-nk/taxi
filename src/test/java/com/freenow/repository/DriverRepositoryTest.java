package com.freenow.repository;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.DriverDO;
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
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void getDriverById() {
        Optional<DriverDO> driverDO = driverRepository.findById(4L);
        Assertions.assertThat(driverDO.isPresent()).isTrue();
        Assertions.assertThat(driverDO.get().getId()).isEqualTo(4);
    }

    @Test
    public void getDriverDoesNotExistById(){
        Optional<DriverDO> driverDO = driverRepository.findById(222L);
        Assertions.assertThat(driverDO.isPresent()).isFalse();
    }

    @Test
    public void getAllDrivers(){
        List<DriverDO> drivers = Lists.newArrayList(driverRepository.findAll());
        Assertions.assertThat(drivers.size()).isEqualTo(10);
    }

    @Test
    public void getDriverByUsername() {
        DriverDO driverDO = driverRepository.findByUsername("driver04");
        Assertions.assertThat(driverDO).isNotNull();
    }

}
