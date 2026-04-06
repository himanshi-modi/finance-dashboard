package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FinancialRecordRequestDto {


    private String description;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private RecordType type;

    @NotNull
    private String category;

    @NotNull
    private LocalDateTime date;
}
