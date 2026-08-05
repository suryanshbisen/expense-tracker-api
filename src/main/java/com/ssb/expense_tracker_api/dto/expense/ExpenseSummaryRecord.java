package com.ssb.expense_tracker_api.dto.expense;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ExpenseSummaryRecord(
        BigDecimal totalAmount,
        Map<String,BigDecimal> categoryBreakdown,
        List<MonthlyTotal> monthlyTrend
) {
    public record MonthlyTotal(String month,BigDecimal total){}
}
