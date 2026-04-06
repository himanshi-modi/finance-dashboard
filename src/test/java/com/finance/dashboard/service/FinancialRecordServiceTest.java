package com.finance.dashboard.service;

import com.finance.dashboard.dto.FinancialRecordRequestDto;
import com.finance.dashboard.entity.enums.RecordType;
import com.finance.dashboard.repository.FinancialRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class FinancialRecordServiceTest {

    @Autowired
    private FinancialRecordService service;

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    private String userId;

    @BeforeEach
    void setup() {
        financialRecordRepository.deleteAll();
        userId = "test-user";
    }

    @Test
    void testCreateRecord() {

        FinancialRecordRequestDto dto = new FinancialRecordRequestDto();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setCategory("Food");
        dto.setType(RecordType.EXPENSE);
        dto.setDate(LocalDateTime.now());
        dto.setDescription("Lunch");

        var response = service.create(userId, dto);

        assertNotNull(response);
        assertEquals("Food", response.getCategory());
        assertEquals(RecordType.EXPENSE, response.getType());
    }

    @Test
    void testGetAllRecords() {

        // create one record
        testCreateRecord();

        var page = service.getAllRecords(userId, PageRequest.of(0, 10));

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void testUpdateRecord() {

        FinancialRecordRequestDto dto = new FinancialRecordRequestDto();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setCategory("Food");
        dto.setType(RecordType.EXPENSE);
        dto.setDate(LocalDateTime.now());

        var created = service.create(userId, dto);

        FinancialRecordRequestDto updateDto = new FinancialRecordRequestDto();
        updateDto.setCategory("Updated Category");

        var updated = service.update(created.getId(), updateDto, userId);

        assertEquals("Updated Category", updated.getCategory());
    }

    @Test
    void testDeleteRecord() {

        FinancialRecordRequestDto dto = new FinancialRecordRequestDto();
        dto.setAmount(BigDecimal.valueOf(500));
        dto.setCategory("Travel");
        dto.setType(RecordType.EXPENSE);
        dto.setDate(LocalDateTime.now());

        var created = service.create(userId, dto);

        service.delete(created.getId(), userId);

        var page = service.getAllRecords(userId, PageRequest.of(0, 10));

        assertEquals(0, page.getTotalElements());
    }


}
