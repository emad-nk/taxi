package com.freenow.domainobject;

import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "car",
        uniqueConstraints = @UniqueConstraint(name = "license_plate", columnNames = {"licensePlate"})
)
public class CarDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime dateCarCreated = ZonedDateTime.now();

    @Column(nullable = false)
    private short seatCount;

    @Column(nullable = false)
    private boolean convertible = false;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private ZonedDateTime dateRatingUpdated = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngineType engineType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    private boolean carSelectedByDriver = false;


    public CarDO() {
    }


    public CarDO(String licensePlate,
                 short seatCount,
                 boolean convertible,
                 EngineType engineType,
                 Manufacturer manufacturer,
                 Rating rating) {

        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.deleted = false;
        this.convertible = convertible;
        this.dateRatingUpdated = null;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
        this.rating = rating;
        this.carSelectedByDriver = false;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }


    public ZonedDateTime getDateCarCreated() {
        return dateCarCreated;
    }

    public void setDateCarCreated(ZonedDateTime dateCarCreated) {
        this.dateCarCreated = dateCarCreated;
    }

    public short getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(short seatCount) {
        this.seatCount = seatCount;
    }

    public boolean isConvertible() {
        return convertible;
    }

    public void setConvertible(boolean convertible) {
        this.convertible = convertible;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getDateRatingUpdated() {
        return dateRatingUpdated;
    }

    public void setDateRatingUpdated(ZonedDateTime dateRatingUpdated) {
        this.dateRatingUpdated = dateRatingUpdated;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCarSelectedByDriver(boolean carSelectedByDriver) {
        this.carSelectedByDriver = carSelectedByDriver;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public boolean isCarSelectedByDriver() {
        return carSelectedByDriver;
    }
}
