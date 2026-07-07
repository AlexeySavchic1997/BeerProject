package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetBeerBrandResponse;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.exception.BeerBrandAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.BeerBrandNotFoundException;
import by.alexeysavchic.beer_pet_project.mapper.BeerMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerBrandRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.BeerBrandServiceImpl;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.BeerBrandSpecifications;
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
public class BeerBrandServiceTest {

    @Mock
    private BeerBrandRepository beerBrandRepository;

    @Mock
    private BeerMapper beerMapper;

    @Mock
    private BeerBrandSpecifications specifications;

    @InjectMocks
    private BeerBrandServiceImpl beerBrandService;

    @Nested
    class addBeerBrandTests {

        @Test
        void successfulAddBeerBrandTest() {
            AddBeerBrandRequest request = new AddBeerBrandRequest();
            request.setBrandName("Guinness");

            BeerBrand mappedBrand = new BeerBrand();
            mappedBrand.setBrandName("Guinness");

            when(beerBrandRepository.existsByBrandName("Guinness")).thenReturn(false);
            when(beerMapper.addBeerBrandInDBRequest(request)).thenReturn(mappedBrand);

            beerBrandService.addBeerBrand(request);

            verify(beerBrandRepository, times(1)).save(mappedBrand);
        }

        @Test
        void throwsExceptionWhenBeerBrandAlreadyExists() {
            AddBeerBrandRequest request = new AddBeerBrandRequest();
            request.setBrandName("Guinness");

            when(beerBrandRepository.existsByBrandName("Guinness")).thenReturn(true);

            assertThrows(BeerBrandAlreadyExistsException.class, () -> beerBrandService.addBeerBrand(request));

            // Проверка: сохранение не должно вызываться
            verify(beerBrandRepository, never()).save(any());
            verify(beerMapper, never()).addBeerBrandInDBRequest(any());
        }
    }

    @Nested
    class deleteBeerBrandTests {

        @Test
        void successfulDeleteBeerBrandTest() {
            BeerBrand beerBrand = new BeerBrand();
            beerBrand.setBrandName("Heineken");

            when(beerBrandRepository.findByBrandName("Heineken")).thenReturn(Optional.of(beerBrand));

            beerBrandService.deleteBeerBrand("Heineken");

            verify(beerBrandRepository, times(1)).delete(beerBrand);
        }

        @Test
        void throwsExceptionWhenBeerBrandNotFound() {
            when(beerBrandRepository.findByBrandName("Heineken")).thenReturn(Optional.empty());

            assertThrows(BeerBrandNotFoundException.class, () -> beerBrandService.deleteBeerBrand("Heineken"));

            verify(beerBrandRepository, never()).delete(any(BeerBrand.class));
        }
    }

    @Nested
    class getBeerBrandsTests {

        @Test
        @SuppressWarnings("unchecked")
        void successfulGetBeerBrandsTest() {
            String searchName = "Paulaner";
            Pageable pageable = PageRequest.of(0, 10);

            BeerBrand beerBrand = new BeerBrand();
            beerBrand.setBrandName("Paulaner");

            GetBeerBrandResponse responseDto = new GetBeerBrandResponse();
            responseDto.setBrandName("Paulaner");

            Specification<BeerBrand> mockSpec = mock(Specification.class);
            when(specifications.getNameSpecification(searchName)).thenReturn(mockSpec);

            Page<BeerBrand> brandPage = new PageImpl<>(List.of(beerBrand), pageable, 1);

            when(beerBrandRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(brandPage);
            when(beerMapper.beerBrandToBeerBrandResponse(beerBrand)).thenReturn(responseDto);


            Page<GetBeerBrandResponse> result = beerBrandService.getBeerBrands(searchName, pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("Paulaner", result.getContent().get(0).getBrandName());

            verify(beerBrandRepository, times(1)).findAll(any(Specification.class), eq(pageable));
            verify(beerMapper, times(1)).beerBrandToBeerBrandResponse(beerBrand);
        }
    }
}