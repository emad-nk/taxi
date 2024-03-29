package com.freenow.controller;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.service.driver.DriverService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController {

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService) {
        this.driverService = driverService;
    }

    @ApiOperation("Get driver by ID")
    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO getDriver(@PathVariable long driverId) {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @ApiOperation("Create driver")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @ApiOperation("Delete driver by ID")
    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable long driverId) {
        driverService.delete(driverId);
    }


    @ApiOperation("Update driver location by ID")
    @PutMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO updateLocation(
            @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude) {
        return DriverMapper.makeDriverDTO(driverService.updateLocation(driverId, longitude, latitude));
    }


    @ApiOperation("Update driver online status")
    @PutMapping("/status/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO updateOnlineStatus(@PathVariable long driverId, @RequestParam OnlineStatus onlineStatus) {
        return DriverMapper.makeDriverDTO(driverService.updateOnlineStatus(driverId, onlineStatus));
    }


    @ApiOperation("Select a car for a driver")
    @PutMapping("/select")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO selectCarByDriver(@RequestParam long driverId, @RequestParam long carId) {
        return DriverMapper.makeDriverDTO(driverService.selectCarByDriver(driverId, carId));
    }


    @ApiOperation("Deselect car for a driver")
    @PutMapping("/deselect")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO deSelectCarByDriver(@RequestParam long driverId) {
        return DriverMapper.makeDriverDTO(driverService.deSelectCarByDriver(driverId));
    }

    @ApiOperation("Search drivers by their attributes or their car characteristics")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<DriverDTO> searchDrivers(@RequestParam Map<String, String> queryParam) {
        return DriverMapper.makeDriverDTOList(driverService.searchDrivers(Collections.unmodifiableMap(queryParam)));
    }

}
