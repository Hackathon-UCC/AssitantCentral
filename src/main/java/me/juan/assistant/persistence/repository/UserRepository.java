package me.juan.assistant.persistence.repository;

import me.juan.assistant.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findUserByEmailIgnoreCase(String email);

}
