package com.freenow.query;

import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.SearchException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.Map;
import java.util.Optional;

public class SearchCriteria {

    private final static String CAR_FIELD = "carDO";

    public static Specification<DriverDO> getDriversBySpecification(Map<String, String> queryParams) {
        Optional<Specification<DriverDO>> reduce = queryParams.keySet().stream().map(k -> {
            if (k.toLowerCase().contains("username")) {
                return getDriversByUsername(queryParams.get(k));
            } else if (k.toLowerCase().contains("seatcount")) {
                return getDriversByCarSeat(queryParams.get(k));
            } else if (k.toLowerCase().contains("onlinestatus")) {
                return getDriversByOnlineStatus(queryParams.get(k));
            } else if (k.toLowerCase().contains("licenseplate")) {
                return getDriversByLicensePlate(queryParams.get(k));
            } else if (k.toLowerCase().contains("converitble")) {
                return getDriversByCarConvertibleType(queryParams.get(k));
            } else if (k.toLowerCase().contains("enginetype")) {
                return getDriversByCarEngineType(queryParams.get(k));
            } else if (k.toLowerCase().contains("rating")) {
                return getDriversByCarRating(queryParams.get(k));
            } else if (k.toLowerCase().contains("manufacturer")) {
                return getDriversByCarManufacturer(queryParams.get(k));
            }
            return null;

        }).reduce(Specification::and);
        return reduce.orElseThrow(() -> new SearchException("Could not perform any search with provided query"));
    }

    private static Specification<DriverDO> getDriversByUsername(String username) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username));
    }

    private static Specification<DriverDO> getDriversByOnlineStatus(String onlineStatus) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("onlineStatus"), onlineStatus));
    }

    private static Specification<DriverDO> getDriversByLicensePlate(String licensePlate) {
        return specificationWithCarJoin("licensePlate", licensePlate);
    }

    private static Specification<DriverDO> getDriversByCarSeat(String seatCount) {
        return specificationWithCarJoin("seatCount", Short.valueOf(seatCount));
    }

    private static Specification<DriverDO> getDriversByCarConvertibleType(String convertible) {
        return specificationWithCarJoin("convertible", Boolean.valueOf(convertible));
    }

    private static Specification<DriverDO> getDriversByCarEngineType(String engineType) {
        return specificationWithCarJoin("engineType", EngineType.valueOf(engineType.toUpperCase()));
    }

    private static Specification<DriverDO> getDriversByCarRating(String rating) {
        return specificationWithCarJoin("rating", Rating.valueOf(rating.toUpperCase()));
    }

    private static Specification<DriverDO> getDriversByCarManufacturer(String manufacturer) {
        return specificationWithCarJoin("manufacturer", Manufacturer.valueOf(manufacturer.toUpperCase()));
    }

    private static Specification<DriverDO> specificationWithCarJoin(String getField, Object field) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<DriverDO, CarDO> carJoin = root.join(CAR_FIELD);
            return criteriaBuilder.equal(carJoin.get(getField), field);
        };
    }
}
