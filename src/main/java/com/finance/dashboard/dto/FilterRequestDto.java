package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FilterRequestDto {
    private String userId;
    @NotNull
    private String category;

    @NotNull
    private RecordType type;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
