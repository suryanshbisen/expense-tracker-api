package com.ssb.expense_tracker_api.dto.expense;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        BigDecimal amount,
        String currency,
        String description,
        LocalDate expenseDate,
        UUID categoryId,
        String categoryName,
        Instant createdAt,
        Instant updatedAt
) {}
