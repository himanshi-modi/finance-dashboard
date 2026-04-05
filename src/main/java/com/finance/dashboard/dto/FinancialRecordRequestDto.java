package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FinancialRecordRequestDto {

    private String description;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private LocalDateTime date;
}
