package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by jt on 2019-06-06.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false ")
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        log.info("Iniciando a busca de uma cerveja");
        var beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        return showInventoryOnHand? beerMapper.beerToBeerDtoWithInventory(beer) : beerMapper.beerToBeerDto(beer);
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        log.info("Iniciando processo de listagem das cervejas.");
        BeerPagedList beerPagedList;
        Page<Beer> beerPage = null;

        if(!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
            return createBeerPagedList(beerPage, showInventoryOnHand);
        }

        if(!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)){
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
            return createBeerPagedList(beerPage, showInventoryOnHand);
        }

        if(StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
            return createBeerPagedList(beerPage, showInventoryOnHand);
        }

        return createBeerPagedList(beerRepository.findAll(pageRequest), showInventoryOnHand);
    }

    @Cacheable(cacheNames = "beerUpcCache", key = "#upc")
    @Override
    public BeerDto getByUpc(String upc) {
        return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc));
    }

    private BeerPagedList createBeerPagedList(Page<Beer> beerPage, Boolean showBeerInventory) {
        return new BeerPagedList(
                beerPage.getContent()
                .stream()
                .map(beer -> {
                    if(showBeerInventory) {
                        return beerMapper.beerToBeerDtoWithInventory(beer);
                    }

                    return beerMapper.beerToBeerDto(beer);
                })
                .collect(Collectors.toList()),
                PageRequest.of(beerPage.getPageable().getPageNumber(), beerPage.getPageable().getPageSize()),
                beerPage.getTotalElements());
    }
}
