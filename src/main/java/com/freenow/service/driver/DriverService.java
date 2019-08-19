package com.freenow.service.driver;

import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;

import java.util.List;
import java.util.Map;

public interface DriverService {

    DriverDO find(Long driverId);

    DriverDO create(DriverDO driverDO);

    void delete(Long driverId);

    DriverDO updateLocation(long driverId, double longitude, double latitude);

    DriverDO updateOnlineStatus(long driverId, OnlineStatus onlineStatus);

    DriverDO selectCarByDriver(long driverId, long carId);

    DriverDO deSelectCarByDriver(long driverId);

    List<DriverDO> searchDrivers(Map<String, String> queryParams);

}
