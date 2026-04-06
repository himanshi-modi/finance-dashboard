package com.finance.dashboard.controller;

import com.finance.dashboard.dto.FilterRequestDto;
import com.finance.dashboard.dto.FinancialRecordRequestDto;
import com.finance.dashboard.dto.FinancialRecordResponseDto;
import com.finance.dashboard.security.CustomUserDetails;
import com.finance.dashboard.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import  org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;
    @Operation(summary = "Create financial record")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FinancialRecordResponseDto> createRecord(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody FinancialRecordRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                financialRecordService.create(user.getUser().getId(), requestDto)
        );
    }

    @Operation(summary = "Get all financial records of logged-in user")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping
    public ResponseEntity<Page<FinancialRecordResponseDto>> getAllRecords(
            @AuthenticationPrincipal CustomUserDetails user,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                financialRecordService.getAllRecords(user.getUser().getId(), pageable)
        );
    }

    @Operation(summary = "Update financial record (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecordResponseDto> updateRecord(
            @PathVariable String id, String userId,
            @RequestBody FinancialRecordRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                financialRecordService.update(id, requestDto,userId)
        );
    }

    @Operation(summary = "Soft delete financial record (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable String id,String userId) {
        financialRecordService.delete(id,userId);
        return ResponseEntity.ok("Record deleted");
    }

    @Operation(summary = "Filter financial records")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @PostMapping("/filter")
    public ResponseEntity<Page<FinancialRecordResponseDto>> filterRecords(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody FilterRequestDto filterRequestDto,
            Pageable pageable
    ) {
        filterRequestDto.setUserId(user.getUser().getId());

        return ResponseEntity.ok(
                financialRecordService.filter(filterRequestDto, pageable)
        );
    }

}
