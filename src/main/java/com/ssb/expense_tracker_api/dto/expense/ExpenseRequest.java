package com.ssb.expense_tracker_api.dto.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(
        @NotNull @DecimalMin(value = "0.1") BigDecimal amount,
        @Size(min = 3, max = 3) String currency,
        @Size(max = 500) String description,
        @NotNull @PastOrPresent LocalDate expenseDate,
        UUID categoryId
) {}
