package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.GeoCoordinate;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO {

    @JsonIgnore
    private Long id;

    @ApiModelProperty(example = "username", required = true)
    @NotNull(message = "Username can not be null!")
    private String username;

    @ApiModelProperty(example = "password", required = true)
    @NotNull(message = "Password can not be null!")
    private String password;

    private GeoCoordinate coordinate;

    private CarDTO car;


    private DriverDTO() {
    }


    private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coordinate = coordinate;
    }


    public static DriverDTOBuilder newBuilder() {
        return new DriverDTOBuilder();
    }


    @JsonProperty
    @ApiModelProperty(hidden = true)
    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public CarDTO getCar() {
        return car;
    }

    public static class DriverDTOBuilder {
        private Long id;
        private String username;
        private String password;
        private GeoCoordinate coordinate;
        private CarDTO carDTO;


        public DriverDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }


        public DriverDTOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }


        public DriverDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }


        public DriverDTOBuilder setCoordinate(GeoCoordinate coordinate) {
            this.coordinate = coordinate;
            return this;
        }

        public DriverDTOBuilder setCarDTO(CarDTO carDTO) {
            this.carDTO = carDTO;
            return this;
        }


        public DriverDTO createDriverDTO() {
            return new DriverDTO(id, username, password, coordinate);
        }

    }
}
