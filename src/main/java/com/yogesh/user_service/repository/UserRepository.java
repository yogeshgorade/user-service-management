package com.yogesh.user_service.repository;

import com.yogesh.user_service.dto.UserResponse;
import com.yogesh.user_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
       OR u.mobile LIKE CONCAT('%', :search, '%')
""")
    Page<User> searchUsers(
            @Param("search") String search,
            Pageable pageable);
}