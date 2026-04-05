package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FinancialRecordResponseDto {
    private String id;
    private Double amount;
    private RecordType type;
    private String category;
    private LocalDateTime date;


}
