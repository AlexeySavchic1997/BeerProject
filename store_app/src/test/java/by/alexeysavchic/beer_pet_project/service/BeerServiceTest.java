package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.request.BeerCharacteristicsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetBeerResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.exception.BeerNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.UnknownBeerBrandException;
import by.alexeysavchic.beer_pet_project.mapper.BeerMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerBrandRepository;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.BeerServiceImpl;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.BeerSpecifications;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    @Mock
    private BeerMapper mapper;

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerBrandRepository beerBrandRepository;

    @Mock
    private BeerSpecifications specifications;

    @InjectMocks
    private BeerServiceImpl beerService;

    @Nested
    class addNewBeerTests {

        @Test
        void successfulAddNewBeerTest() {
            AddBeerRequest request = new AddBeerRequest();
            request.setBeerBrand("Guinness");

            BeerBrand beerBrand = new BeerBrand();
            beerBrand.setBrandName("Guinness");

            Beer mappedBeer = new Beer();

            when(beerBrandRepository.findByBrandName("Guinness")).thenReturn(Optional.of(beerBrand));
            when(mapper.AddNewBeerToBeer(request)).thenReturn(mappedBeer);

            beerService.addNewBeer(request);

            assertEquals(beerBrand, mappedBeer.getBeerBrand());
            verify(beerRepository, times(1)).save(mappedBeer);
        }

        @Test
        void throwsExceptionWhenBeerBrandNotFound() {
            AddBeerRequest request = new AddBeerRequest();
            request.setBeerBrand("Unknown Brand");

            when(beerBrandRepository.findByBrandName("Unknown Brand")).thenReturn(Optional.empty());

            assertThrows(UnknownBeerBrandException.class, () -> beerService.addNewBeer(request));

            verify(mapper, never()).AddNewBeerToBeer(any());
            verify(beerRepository, never()).save(any());
        }
    }

    @Nested
    class deleteBeerTests {

        @Test
        void successfulDeleteBeerTest() {
            Beer beer = new Beer();
            beer.setSku("BEER-123");

            when(beerRepository.findBeerBySku("BEER-123")).thenReturn(Optional.of(beer));

            beerService.deleteBeer("BEER-123");

            verify(beerRepository, times(1)).delete(beer);
        }

        @Test
        void throwsExceptionWhenBeerNotFound() {
            when(beerRepository.findBeerBySku("BEER-123")).thenReturn(Optional.empty());

            assertThrows(BeerNotFoundException.class, () -> beerService.deleteBeer("BEER-123"));

            verify(beerRepository, never()).delete(any(Beer.class));
        }
    }

    @Nested
    class findAllTests {

        @Test
        @SuppressWarnings("unchecked")
        void successfulFindAllTest() {
            GetBeerRequest request = new GetBeerRequest();
            BeerCharacteristicsRequest charRequest = new BeerCharacteristicsRequest();
            request.setCharacteristics(List.of(charRequest));

            Pageable pageable = PageRequest.of(0, 10);

            Beer beer = new Beer();
            GetBeerResponse responseDto = new GetBeerResponse();

            Page<Beer> beerPage = new PageImpl<>(List.of(beer), pageable, 1);

            Specification<Beer> mockSpec = mock(Specification.class);
            when(specifications.getIdSpecification(any())).thenReturn(mockSpec);
            when(specifications.getSkuSpecification(any())).thenReturn(mockSpec);
            when(specifications.getNameSpecification(any())).thenReturn(mockSpec);
            when(specifications.getVolumeSpecification(any())).thenReturn(mockSpec);
            when(specifications.getPriceSpecification(any(), any())).thenReturn(mockSpec);
            when(specifications.getBrandNameSpecification(any())).thenReturn(mockSpec);
            when(specifications.getCharacteristicSpecification(any(), any(), any())).thenReturn(mockSpec);
            when(specifications.getAccessibleBeer()).thenReturn(mockSpec);

            when(beerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(beerPage);
            when(mapper.beerToBeerResponse(beer)).thenReturn(responseDto);

            Page<GetBeerResponse> result = beerService.findAll(request, pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());

            verify(beerRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            verify(mapper, times(1)).beerToBeerResponse(beer);
        }

        @Test
        @SuppressWarnings("unchecked")
        void successfulFindAllWithoutCharacteristicsTest() {
            GetBeerRequest request = new GetBeerRequest();
            request.setCharacteristics(new ArrayList<>()); // Пустой список

            Pageable pageable = PageRequest.of(0, 10);
            Page<Beer> beerPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

            Specification<Beer> mockSpec = mock(Specification.class);
            when(specifications.getIdSpecification(any())).thenReturn(mockSpec);
            when(specifications.getSkuSpecification(any())).thenReturn(mockSpec);
            when(specifications.getNameSpecification(any())).thenReturn(mockSpec);
            when(specifications.getVolumeSpecification(any())).thenReturn(mockSpec);
            when(specifications.getPriceSpecification(any(), any())).thenReturn(mockSpec);
            when(specifications.getBrandNameSpecification(any())).thenReturn(mockSpec);
            when(specifications.getAccessibleBeer()).thenReturn(mockSpec);

            when(beerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(beerPage);

            Page<GetBeerResponse> result = beerService.findAll(request, pageable);

            assertNotNull(result);
            assertEquals(0, result.getTotalElements());

            verify(specifications, never()).getCharacteristicSpecification(any(), any(), any());
        }
    }
}
