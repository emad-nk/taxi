package com.freenow.repository;

import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * This test class is based on the data inserted in the DB by data.sql file under resources
 */
public class DriverRepositoryTest extends RepositoryTestData {

    @Autowired
    private DriverRepository driverRepository;


    @Test
    public void getDriverById() {
        Optional<DriverDO> driverDO = driverRepository.findById(4L);
        Assertions.assertThat(driverDO.isPresent()).isTrue();
    }

    @Test
    public void testDriverByOnlineStatus() {
        List<DriverDO> onlineDrivers = driverRepository.findByOnlineStatus(OnlineStatus.ONLINE);
        Assertions.assertThat(onlineDrivers).hasSize(6);
    }

    @Test
    public void testDriverByOfflineStatus() {
        List<DriverDO> offlineDrivers = driverRepository.findByOnlineStatus(OnlineStatus.OFFLINE);
        Assertions.assertThat(offlineDrivers).hasSize(4);
    }

    @Test
    public void getDriverByUsername() {
        DriverDO driverDO = driverRepository.findByUsername("driver04");
        Assertions.assertThat(driverDO).isNotNull();
    }

}
