package com.ead.authuser.repositories;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = "roles",type = EntityGraph.EntityGraphType.FETCH) // In a specific method I need a EAGER query.(roles = UserModel Relation  to RoleModel)
    Optional<UserModel> findByUsername(String username); // Bring the User with  Roles !!
    @EntityGraph(attributePaths = "roles",type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findById(UUID userId);
}
