package by.alexeysavchic.beer_pet_project.repository;

import by.alexeysavchic.beer_pet_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    public Optional<User> findUserById(Long id);

    public Optional<User> findUserByUsername(String username);

    public Optional<User> findUserByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByUsername(String username);
}
