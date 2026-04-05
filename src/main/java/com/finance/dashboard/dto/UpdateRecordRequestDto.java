package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateRecordRequestDto {
    private String description;
    private Double amount;
    private RecordType type;
    private String category;
    private LocalDateTime date;

}
