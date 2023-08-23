package com.bekamdo.repositories;

import com.bekamdo.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer,Integer> {
}
