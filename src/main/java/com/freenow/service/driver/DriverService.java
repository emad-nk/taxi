package com.freenow.service.driver;

import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;

import java.util.List;

public interface DriverService {

    DriverDO find(Long driverId);

    DriverDO create(DriverDO driverDO);

    List<DriverDO> find(OnlineStatus onlineStatus);

    void delete(Long driverId);

    void updateLocation(long driverId, double longitude, double latitude);

    void selectCarByDriver(long driverId, long carId);

    void deSelectCarByDriver(long driverId);

}
