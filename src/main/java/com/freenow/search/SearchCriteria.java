package com.freenow.search;

import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.domainvalue.Rating;
import com.freenow.exception.ParseValueException;
import com.freenow.exception.SearchException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SearchCriteria {

    private final static String CAR_FIELD = "carDO";

    public static Specification<DriverDO> getDriversBySpecification(Map<String, String> queryParams) {

        Optional<Specification<DriverDO>> reduce = queryParams.keySet().stream()
                .map(k -> getSpecification(k).apply(queryParams.get(k)))
                .reduce(Specification::and);

        return reduce.orElseThrow(() -> new SearchException("Could not perform any search with provided query"));
    }

    private static Function<String, Specification<DriverDO>> getSpecification(String key) {
        switch (key.trim().toLowerCase()) {
            case "username":
                return SearchCriteria::getDriversByUsername;
            case "onlinestatus":
                return SearchCriteria::getDriversByOnlineStatus;
            case "licenseplate":
                return SearchCriteria::getDriversByLicensePlate;
            case "seatcount":
                return SearchCriteria::getDriversByCarSeat;
            case "convertible":
                return SearchCriteria::getDriversByCarConvertibleType;
            case "enginetype":
                return SearchCriteria::getDriversByCarEngineType;
            case "rating":
                return SearchCriteria::getDriversByCarRating;
            case "manufacturer":
                return SearchCriteria::getDriversByCarManufacturer;
        }
        throw new SearchException("Could not perform any search with provided query");
    }

    private static Specification<DriverDO> getDriversByUsername(String username) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username));
    }

    private static Specification<DriverDO> getDriversByOnlineStatus(String onlineStatus) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("onlineStatus")
                , parseEnum(onlineStatus, OnlineStatus.class)));
    }

    private static Specification<DriverDO> getDriversByLicensePlate(String licensePlate) {
        return specificationWithCarJoin("licensePlate", licensePlate.trim());
    }

    private static Specification<DriverDO> getDriversByCarSeat(String seatCount) {
        return specificationWithCarJoin("seatCount", parseShort(seatCount));
    }

    private static Specification<DriverDO> getDriversByCarConvertibleType(String convertible) {
        return specificationWithCarJoin("convertible", parseBoolean(convertible));
    }

    private static Specification<DriverDO> getDriversByCarEngineType(String engineType) {
        return specificationWithCarJoin("engineType", parseEnum(engineType, EngineType.class));
    }

    private static Specification<DriverDO> getDriversByCarRating(String rating) {
        return specificationWithCarJoin("rating", parseEnum(rating, Rating.class));
    }

    private static Specification<DriverDO> getDriversByCarManufacturer(String manufacturer) {
        return specificationWithCarJoin("manufacturer", parseEnum(manufacturer, Manufacturer.class));
    }

    private static Specification<DriverDO> specificationWithCarJoin(String getField, Object field) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<DriverDO, CarDO> carJoin = root.join(CAR_FIELD);
            return criteriaBuilder.equal(carJoin.get(getField), field);
        };
    }

    private static <T extends Enum<T>> T parseEnum(String value, Class<T> clazz) {
        String val = value.trim().toUpperCase();
        try {
            return Enum.valueOf(clazz, val);
        } catch (Exception ex) {
            throw new ParseValueException(String.format("Could not parse the given value %s to its type", value));
        }
    }

    private static short parseShort(String value) {
        String val = value.trim().toUpperCase();
        try {
            return Short.valueOf(val);
        } catch (Exception ex) {
            throw new ParseValueException(String.format("Could not parse the given value %s to its type", value));
        }
    }

    private static Boolean parseBoolean(String value) {
        String val = value.trim().toLowerCase();
        try {
            return Boolean.valueOf(val);
        } catch (Exception ex) {
            throw new ParseValueException(String.format("Could not parse the given value %s to its type", value));
        }
    }
}
