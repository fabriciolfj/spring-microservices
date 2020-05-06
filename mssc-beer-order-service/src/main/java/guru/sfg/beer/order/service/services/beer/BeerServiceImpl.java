package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService{
    private final String BEER_PATH_V1 = "/api/v1/beer/";
    private final String BEER_UPC_PATH_v1 = "/api/v1/beerUpc/";
    private final RestTemplate restTemplate;

    private String beerServiceHost;

    @Override
    public Optional<BeerDto> getBeerById(UUID uuid) {
        return Optional.of(restTemplate.getForObject(beerServiceHost + BEER_PATH_V1 + uuid.toString(), BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        return Optional.of(restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH_v1 + upc, BeerDto.class));
    }

    public void setBeerServiceHost(String beerServiceHost) {
        this.beerServiceHost = beerServiceHost;
    }
}
