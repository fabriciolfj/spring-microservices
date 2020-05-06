package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public abstract class BeerMapperDecorator implements BeerMapper {
    @Autowired
    private BeerInventoryService beerInventoryService;
    @Autowired
    private BeerMapper mapper;

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        return mapper.beerToBeerDto(beer);
    }

    @Override
    public BeerDto beerToBeerDtoWithInventory(Beer beer) {
        var dto = mapper.beerToBeerDto(beer);
        dto.setQuantityOnHand(beerInventoryService.getOnhandInventory(dto.getId()));
        return dto;
    }

    @Override
    public Beer beerDtoToBeer(BeerDto dto) {
        return mapper.beerDtoToBeer(dto);
    }
}
