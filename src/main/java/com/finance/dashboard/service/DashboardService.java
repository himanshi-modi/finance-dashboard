package com.finance.dashboard.service;

import com.finance.dashboard.dto.DashboardResponseDto;
import com.finance.dashboard.dto.FinancialRecordResponseDto;
import com.finance.dashboard.entity.enums.RecordType;
import com.finance.dashboard.entity.model.FinancialRecord;
import com.finance.dashboard.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final FinancialRecordRepository repository;
    private final ModelMapper modelMapper;

    public DashboardResponseDto getDashboard(String userId) {

        List<FinancialRecord> records =
                repository.findByUserIdAndIsDeletedFalse(userId);
        BigDecimal totalIncome = records.stream()
                .filter(r -> r.getRecordType() == RecordType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = records.stream()
                .filter(r -> r.getRecordType() == RecordType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        Map<String, BigDecimal> categoryWise = records.stream()
                .collect(Collectors.groupingBy(
                        FinancialRecord::getCategory,
                        Collectors.mapping(
                                FinancialRecord::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        List<FinancialRecordResponseDto> recent = records.stream()
                .sorted(Comparator.comparing(FinancialRecord::getDate).reversed())
                .limit(5)
                .map(r -> modelMapper.map(r, FinancialRecordResponseDto.class))
                .toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, BigDecimal> monthlyTrend = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getDate().format(formatter),
                        Collectors.mapping(
                                FinancialRecord::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        return DashboardResponseDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .categoryWiseTotals(categoryWise)
                .recentTransactions(recent)
                .monthlyTrend(monthlyTrend)
                .build();
    }
}
