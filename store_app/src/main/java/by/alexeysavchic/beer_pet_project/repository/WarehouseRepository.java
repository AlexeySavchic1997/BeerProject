package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseBeerInfo, Long>
{
    @Query(value = "select w from WarehouseBeerInfo w join fetch w.beer b " +
            "where w.zoneType='SORTING' and w.zoneType='UNLOADING' and b.id in :ids")
    public List<WarehouseBeerInfo> findAllByBeerIdInSortingAndUnloadingZone(@Param("ids") List<Long> ids);
}
