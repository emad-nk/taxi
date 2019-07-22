package com.freenow.service.driver;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarRepository carRepository;

    public DefaultDriverService(final DriverRepository driverRepository, final CarRepository carRepository) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
    }

    /**
     * Selects a driver by id.
     *
     * @param driverId id of the driver provided when creating a new driver
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO driver domain object
     * @return driverDO
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) {
        try {
            return driverRepository.save(driverDO);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId id of the driver provided when creating a new driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) {
        findDriverChecked(driverId).setDeleted(true);
    }


    /**
     * Updates the location for a driver.
     *
     * @param driverId  id of the driver provided when creating a new driver
     * @param longitude location longitude
     * @param latitude  location latitude
     * @throws EntityNotFoundException when cannot find the driver by its ID
     */
    @Override
    @Transactional
    public DriverDO updateLocation(long driverId, double longitude, double latitude) {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
        return driverDO;
    }


    /**
     * Selects a car for a driver
     *
     * @param driverId id of the driver provided when creating a new driver
     * @param carId    id of the car provided when creating a new car
     * @throws ConstraintsViolationException when a driver is offline and wants to select a car or
     *                                       when the driver already has a car
     * @throws CarAlreadyInUseException      when the chosen car by driver is already in use by another driver
     * @throws EntityNotFoundException       when cannot find a driver with given ID or
     *                                       when cannot find a car with given id
     */
    @Override
    @Transactional
    public DriverDO selectCarByDriver(long driverId, long carId) {
        DriverDO driverDO = findDriverChecked(driverId);
        CarDO carDO = findCarChecked(carId);

        if (driverDO.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
            throw new ConstraintsViolationException(String.format("Offline Driver with id {%d} cannot select a car."
                    , driverId));
        }
        if (carDO.isCarSelectedByDriver()) {
            throw new CarAlreadyInUseException(String.format("Selected Car with id {%d} is already use.", carId));
        }
        if (driverDO.getCarDO() != null) {
            throw new ConstraintsViolationException(String.format("Driver with id {%d} already has a car.", driverId));
        }

        carDO.setCarSelectedByDriver(true);
        driverDO.setCarDO(carDO);
        LOGGER.info("car id {} is selected by driver id {}.", carDO.getId(), driverDO.getId());
        return driverDO;
    }


    /**
     * deselects a car for a driver
     *
     * @param driverId id of the driver provided when creating a new driver
     * @throws ConstraintsViolationException when driver does not have a car to deselect
     * @throws EntityNotFoundException       when cannot find a driver by the given ID
     */
    @Override
    @Transactional
    public DriverDO deSelectCarByDriver(long driverId) {
        DriverDO driverDO = findDriverChecked(driverId);
        CarDO carDO = driverDO.getCarDO();
        if (carDO == null) {
            throw new ConstraintsViolationException(String.format("Driver with id {%d} does not have a car " +
                    "to deselect.", driverId));
        }
        carDO.setCarSelectedByDriver(false);
        driverDO.setCarDO(null);
        return driverDO;
    }

    /**
     * Search drivers by criteria
     *
     * @param queryParams map of values containing the client search
     * @return list of drivers
     */
    @Override
    public List<DriverDO> searchDrivers(Map<String, String> queryParams) {
        return driverRepository.findAll(SearchCriteria.getDriversBySpecification(queryParams));
    }

    private DriverDO findDriverChecked(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find driver with id: " + driverId));
    }

    private CarDO findCarChecked(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find car with id: " + carId));
    }

}
