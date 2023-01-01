package com.officemanagementsystemapi.repository;

import com.officemanagementsystemapi.model.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PeopleRepository extends JpaRepository<People, Integer> {
    Optional<People> findFirstByEmail(String email);
    Set<People> findAllByIdIn(List<Integer> ids);
}
