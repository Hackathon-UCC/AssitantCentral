package me.juan.assistant.persistence.repository;

import me.juan.assistant.persistence.entity.UserCampus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCampusRepository extends CrudRepository<UserCampus, Integer> {


}
