package com.freenow.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.Rating;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String BASE_URI = "/v1/cars";
    private HttpHeaders headers = new HttpHeaders();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createACarAFindItDeleteIt() throws Exception {

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        CarDTO carDTO = getCarDTO("LL22222111");
        String jsonInString = mapper.writeValueAsString(carDTO);

        HttpEntity<CarDTO> entity = new HttpEntity<>(carDTO, headers);

        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        ResponseEntity response = restTemplate.postForEntity(BASE_URI, entity, String.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CarDTO carResponse = mapper.readValue(Objects.requireNonNull(response.getBody()).toString(), CarDTO.class);

        Long id = carResponse.getId();
        Assertions.assertThat(id).isNotNull();
        Assertions.assertThat(carResponse.getLicensePlate()).isEqualTo("LL22222111");


        ResponseEntity findResponse = restTemplate
                .getForEntity(BASE_URI + "/" + id,  String.class);

        Assertions.assertThat(findResponse).isNotNull();
        carResponse = mapper.readValue(Objects.requireNonNull(findResponse.getBody()).toString(), CarDTO.class);
        Assertions.assertThat(findResponse).isNotNull();
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(carResponse.getLicensePlate()).isEqualTo("LL22222111");

    }

    public CarDTO getCarDTO(String licensePlate) {
        return CarDTO.newBuilder()
                .setConvertible(true)
                .setEngineType(EngineType.DIESEL)
                .setLicensePlate(licensePlate)
                .setSeatCount((short) 5)
                .setManufacturer(Manufacturer.MERCEDES)
                .setRating(Rating.FOUR)
                .createCarDTO();
    }
}
