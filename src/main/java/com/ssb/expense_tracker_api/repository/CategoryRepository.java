package com.ssb.expense_tracker_api.repository;

import com.ssb.expense_tracker_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByUserIdOrderByNameAsc(UUID userId);

    Optional<Category> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByUserIdAndNameIgnoreCase(UUID id,String name);

    @Query("""
                SELECT c FROM Category c 
                WHERE c.user.id=:userId 
                AND LOWER(c.name) LIKE(CONCAT('%',:keyword,'%'))
                ORDER BY c.name ASC
        """)
    List<Category> searchByUserIdAndKeyword(@Param("userId") UUID userId,@Param("keyword") String keyword);
}
