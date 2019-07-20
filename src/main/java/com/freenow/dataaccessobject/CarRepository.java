package com.freenow.dataaccessobject;

import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.Manufacturer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<CarDO, Long> {

   List<CarDO> findByManufacturer(Manufacturer manufacturer);
}
