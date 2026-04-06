package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateRecordRequestDto {

    private String description;
    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private RecordType type;

    @NotNull
    private String category;

    @NotNull
    private LocalDateTime date;

}
