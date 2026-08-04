package com.ssb.expense_tracker_api.repository;

import com.ssb.expense_tracker_api.entity.Expense;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    Optional<Expense> findByIdAndUserId(UUID id,UUID userId);

    Page<Expense> findAllUserIdOrderByExpenseDateDesc(UUID userId, Pageable pageable);

    @Query("""
        SELECT e FROM Expense e
        WHERE e.user.id=:userId
        AND e.expenseDate BETWEEN :from AND :to
        ORDER BY e.expenseDate DESC
        """)
    Page<Expense> findByUserIdAndDateRange(@Param("userId") UUID userId,
                                           @Param("from") LocalDate from,
                                           @Param("to") LocalDate to,
                                           Pageable pageable);

    List<Expense> findAllByUserIdAndCategoryIdOrderByExpenseDateDesc(UUID userId, UUID categoryId);

    @Query("""
        SELECT COALESCE(SUM(e.amount),0) FROM Expense e
        WHERE e.user.id=:userId
    """)
    BigDecimal sumTotalByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT COALESCE(SUM(e.amount),0) FROM Expense e
        WHERE e.user.id=:userId 
        AND e.expenseDate BETWEEN :from AND :to
    """)
    BigDecimal sumTotalByUserIdAndDateRange(@Param("userId") UUID userId,
                                            @Param("from") LocalDate from,
                                            @Param("to") LocalDate to);

    @Query(value = """
        SELECT c.name AS category_name,
                COALESCE(SUM(e.amount),0) AS total_amount,
                COUNT(e.id) as expense_count
         FROM expenses e
        LEFT JOIN categories c ON c.id=e.category_id
        WHERE e.user_id=:userId
        AND e.expense_date BETWEEN :from AND :to
        GROUP BY c.name
        ORDER BY total_amount DESC
    """, nativeQuery = true)
    List<Object[]> findCategoryBreakdown(@Param("userId") UUID userId,
                                         @Param("from") LocalDate from,
                                         @Param("to") LocalDate to);

    @Query(value = """
        SELECT to_char(date_trunc('month',e.expense_date),'YYYY-MM') AS month,
            COALESCE(SUM(e.amount,0)) AS total_amount
        FROM expenses e
        WHERE e.used_id=:userId
        AND e.expense_date>=(CURRENT_DATE - INTERVAL '12 months')
        GROUP BY date_trunc('month',e.expense_date)
        ORDER BY date_trunc('month',e.expense_date)
    """,nativeQuery = true)
    List<Object[]> findMonthlyTrend(@Param("userId") UUID userId);

}
