package com.bekamdo.contollers;

import com.bekamdo.domain.Beer;
import com.bekamdo.model.BeerDTO;
import com.bekamdo.repositories.BeerRepository;
import com.bekamdo.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {
    @Autowired
    WebTestClient webTestClient ;

    @Test
    void testListBeers() {
        webTestClient.get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type","application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(1)
    void testGetById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID,1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type","application/json")
                .expectBody(BeerDTO.class);
    }
    @Test

    void testGetByIdNotFound() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID,999)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    @Order(2)
    void testUpdateBeer() {
        webTestClient.put().uri(BeerController.BEER_PATH_ID,1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()),BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test

    void testUpdateBeerNotFound() {
        webTestClient.put().uri(BeerController.BEER_PATH_ID,999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()),BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        webTestClient.delete().uri(BeerController.BEER_PATH_ID,1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(3)
    void testCreateBeer() {
        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }
    void testCreateBeerBadData() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type","application/json")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }
    @Test

    void testUpdateBeerBadRequest() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerStyle("");

        webTestClient.put()
                .uri(BeerController.BEER_PATH_ID,1)
                .body(Mono.just(testBeer),BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteNotFound(){
        webTestClient.delete().uri(BeerController.BEER_PATH_ID,999).exchange().expectStatus().isNotFound();
    }
}