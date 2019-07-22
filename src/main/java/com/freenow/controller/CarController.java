package com.freenow.controller;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Rating;
import com.freenow.service.car.CarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * All operations with a car will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }


    @ApiOperation("Get car by ID")
    @GetMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO getCar(@Valid @PathVariable long carId) {
        return CarMapper.makeCarDTO(carService.find(carId));
    }


    @ApiOperation("Create a new car")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));
    }


    @ApiOperation("Delete car by ID")
    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@Valid @PathVariable long carId) {
        carService.delete(carId);
    }


    @ApiOperation("Update car rating by ID")
    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO updateRating(@Valid @PathVariable long carId, @RequestParam Rating rating) {
        return CarMapper.makeCarDTO(carService.updateRating(carId, rating));
    }

}
