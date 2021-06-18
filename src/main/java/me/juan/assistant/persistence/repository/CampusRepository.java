package me.juan.assistant.persistence.repository;

import me.juan.assistant.persistence.entity.Campus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends CrudRepository<Campus, Integer> {

    Optional<Campus> findCampusByDomainIgnoreCase(String domain);

}
