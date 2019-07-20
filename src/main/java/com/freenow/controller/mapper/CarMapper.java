package com.freenow.controller.mapper;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarDO makeCarDO(CarDTO CarDTO) {
        return new CarDO(CarDTO.getLicensePlate(),
                CarDTO.getSeatCount(),
                CarDTO.isConvertible(),
                CarDTO.getEngineType(),
                CarDTO.getManufacturer(),
                CarDTO.getRating());
    }


    public static CarDTO makeCarDTO(CarDO carDO) {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
                .setLicensePlate(carDO.getLicensePlate())
                .setConvertible(carDO.isConvertible())
                .setEngineType(carDO.getEngineType())
                .setManufacturer(carDO.getManufacturer())
                .setRating(carDO.getRating())
                .setSeatCount(carDO.getSeatCount())
                .setId(carDO.getId())
                .setSelectedByDriver(carDO.isCarSelectedByDriver());

        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars) {
        return cars.stream()
                .map(CarMapper::makeCarDTO)
                .collect(Collectors.toList());
    }
}
