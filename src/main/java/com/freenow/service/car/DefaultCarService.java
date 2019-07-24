package com.freenow.service.car;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultCarService implements CarService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultCarService.class);
    private final CarRepository carRepository;

    public DefaultCarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Finds a car by its ID
     *
     * @param carId id of the car provided when the car is created
     * @return the found car
     */
    @Override
    public CarDO find(Long carId) {
        return carRepository.findById(carId).orElseThrow(() ->
                new EntityNotFoundException("Could not find entity with licensePlate: " + carId));
    }

    /**
     * Creates a new car
     *
     * @param carDO car domain object
     * @return the created car with ID
     * @throws ConstraintsViolationException when cannot save the car in the DB
     */
    @Override
    public CarDO create(CarDO carDO) {
        try {
            return carRepository.save(carDO);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
    }


    /**
     * Deletes a car by its ID
     *
     * @param carId id of the car provided when the car is created
     */
    @Override
    @Transactional
    public void delete(Long carId) {
        find(carId).setDeleted(true);
    }

    /**
     * Updates the car rating by ID
     *
     * @param carId  id of the car provided when the car is created
     * @param rating enum type
     */
    @Override
    @Transactional
    public CarDO updateRating(Long carId, Rating rating) {
        CarDO carDO = find(carId);
        carDO.setRating(rating);
        return carDO;
    }

}
