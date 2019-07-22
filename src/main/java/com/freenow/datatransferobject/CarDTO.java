package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class CarDTO {

    @JsonIgnore
    private Long id;

    @ApiModelProperty(example = "LLBB 1111", required = true)
    @NotNull(message = "licensePlate can not be null!")
    private String licensePlate;

    @ApiModelProperty(example = "5", required = true)
    @NotNull(message = "seat count can not be null!")
    private short seatCount;

    @NotNull(message = "convertible can not be null!")
    private boolean convertible = false;

    @ApiModelProperty(example = "DIESEL", required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "engineType can not be null!")
    private EngineType engineType;

    @ApiModelProperty(example = "FOUR", required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "rating can not be null!")
    private Rating rating;

    @ApiModelProperty(example = "MERCEDES", required = true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "manufacturer can not be null!")
    private Manufacturer manufacturer;

    private boolean selectedByDriver = false;


    public CarDTO() {
    }


    private CarDTO(Long id,
                   String licensePlate,
                   short seatCount,
                   boolean convertible,
                   EngineType engineType,
                   Rating rating,
                   Manufacturer manufacturer,
                   boolean selectedByDriver) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.engineType = engineType;
        this.rating = rating;
        this.manufacturer = manufacturer;
        this.convertible = convertible;
        this.selectedByDriver = selectedByDriver;

    }


    public static CarDTOBuilder newBuilder() {
        return new CarDTOBuilder();
    }

    @JsonProperty
    @ApiModelProperty(hidden = true)
    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }


    public short getSeatCount() {
        return seatCount;
    }

    public boolean isConvertible() {
        return convertible;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public Rating getRating() {
        return rating;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public boolean isSelectedByDriver() {
        return selectedByDriver;
    }

    public static class CarDTOBuilder {
        private Long id;
        private String licensePlate;
        private short seatCount;
        private boolean convertible = false;
        private EngineType engineType;
        private Rating rating;
        private Manufacturer manufacturer;
        private boolean selectedByDriver;


        public CarDTOBuilder setId(Long id){
            this.id = id;
            return this;
        }

        public CarDTOBuilder setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
            return this;
        }


        public CarDTOBuilder setSeatCount(short seatCount) {
            this.seatCount = seatCount;
            return this;
        }

        public CarDTOBuilder setConvertible(boolean convertible) {
            this.convertible = convertible;
            return this;
        }

        public CarDTOBuilder setEngineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }

        public CarDTOBuilder setRating(Rating rating) {
            this.rating = rating;
            return this;
        }

        public CarDTOBuilder setManufacturer(Manufacturer manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CarDTOBuilder setSelectedByDriver(boolean selectedByDriver) {
            this.selectedByDriver = selectedByDriver;
            return this;
        }


        public CarDTO createCarDTO() {
            return new CarDTO(id, licensePlate, seatCount, convertible, engineType, rating, manufacturer, selectedByDriver);
        }

    }

    @Override
    public String toString() {
        return "CarDTO{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", seatCount=" + seatCount +
                ", convertible=" + convertible +
                ", engineType=" + engineType +
                ", rating=" + rating +
                ", manufacturer=" + manufacturer +
                ", selectedByDriver=" + selectedByDriver +
                '}';
    }
}
