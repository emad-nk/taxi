package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(example = "username", required = true)
    @NotNull(message = "Username can not be null!")
    private String username;

    @ApiModelProperty(example = "password", required = true)
    @NotNull(message = "Password can not be null!")
    private String password;

    @ApiModelProperty(hidden = true)
    private OnlineStatus onlineStatus;

    @ApiModelProperty(hidden = true)
    private GeoCoordinate coordinate;

    @ApiModelProperty(hidden = true)
    private CarDTO car;


    private DriverDTO() {
    }


    private DriverDTO(Long id,
                      String username,
                      String password,
                      OnlineStatus onlineStatus,
                      GeoCoordinate coordinate,
                      CarDTO car) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coordinate = coordinate;
        this.onlineStatus = onlineStatus;
        this.car = car;
    }


    public static DriverDTOBuilder newBuilder() {
        return new DriverDTOBuilder();
    }


    @JsonProperty
    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    @JsonProperty
    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    @JsonProperty
    public CarDTO getCar() {
        return car;
    }

    @JsonProperty
    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public static class DriverDTOBuilder {
        private Long id;
        private String username;
        private String password;
        private GeoCoordinate coordinate;
        private CarDTO carDTO;
        private OnlineStatus onlineStatus;


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

        public DriverDTOBuilder setOnlineStatus(OnlineStatus onlineStatus) {
            this.onlineStatus = onlineStatus;
            return this;
        }


        public DriverDTO createDriverDTO() {
            return new DriverDTO(id, username, password, onlineStatus, coordinate, carDTO);
        }

    }

    public static Comparator<DriverDTO> idDescending() {
        return (o1, o2) -> o2.id.compareTo(o1.id);
    }
}
