package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FilterRequestDto {
    private String userId;
    private String category;
    private RecordType type;
    private LocalDateTime start;
    private LocalDateTime end;
}
