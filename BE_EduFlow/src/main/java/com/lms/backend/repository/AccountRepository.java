package com.lms.backend.repository;

import com.lms.backend.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(String role);

    long countByRole(String role);

    List<Account> findByRoleOrderByAccountIdDesc(String role);
}
