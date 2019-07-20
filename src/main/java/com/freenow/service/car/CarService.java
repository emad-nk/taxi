package com.freenow.service.car;

import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;

import java.util.List;

public interface CarService {
    CarDO find(Long carId);

    CarDO create(CarDO carDO);

    void delete(Long carId);

    void updateRating(Long carId, Rating rating);

    List<CarDO> findByManufacturer(Manufacturer manufacturer);
}
