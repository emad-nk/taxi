package com.freenow.service.car;

import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Rating;

public interface CarService {
    CarDO find(Long carId);

    CarDO create(CarDO carDO);

    void delete(Long carId);

    CarDO updateRating(Long carId, Rating rating);

}
