package guru.springframework.msscbeerservice.repositories;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {

    Page<Beer> findAllByBeerName(final String beerName, final Pageable pageable);

    Page<Beer> findAllByBeerStyle(final BeerStyleEnum beerStyle, final Pageable pageable);

    Page<Beer> findAllByBeerNameAndBeerStyle(final String beerName, final BeerStyleEnum beerStyle, final Pageable pageable);

    Beer findByUpc(String upc);
}
