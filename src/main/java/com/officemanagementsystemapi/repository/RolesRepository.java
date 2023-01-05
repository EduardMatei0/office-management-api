package com.officemanagementsystemapi.repository;

import com.officemanagementsystemapi.model.EnumRoles;
import com.officemanagementsystemapi.model.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends CrudRepository<Roles, Integer> {
    Optional<Roles> findFirstByName(EnumRoles name);
    @Query(value = "select r.name from Roles r")
    Set<String> getAllRoles();
    List<Roles> findAllByNameIn(List<EnumRoles> names);
}
