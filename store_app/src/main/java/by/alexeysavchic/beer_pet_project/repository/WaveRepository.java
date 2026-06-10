package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaveRepository extends JpaRepository<Wave, Long> {

    @Query("select w.typeOfSubscription from Wave w where w.status ='NEW'")
    public List<TypeOfSubscription> findCreatedNewSubscriptions();

    public Wave findTopByStatus(WaveStatus status);
}
