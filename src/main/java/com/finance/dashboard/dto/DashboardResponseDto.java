package com.finance.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponseDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;

    private Map<String, BigDecimal> categoryWiseTotals;

    private List<FinancialRecordResponseDto> recentTransactions;

    private Map<String, BigDecimal> monthlyTrend;
}
