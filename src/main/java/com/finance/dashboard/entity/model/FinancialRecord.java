package com.finance.dashboard.entity.model;

import com.finance.dashboard.entity.enums.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "financial_records")
public class FinancialRecord {

    @Id
    private String id;

    @Indexed
    private String userId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Indexed
    private RecordType recordType;

    @NotBlank
    @Indexed
    private String category;

    @NotNull
    @Indexed
    private LocalDateTime date;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isDeleted;

}
