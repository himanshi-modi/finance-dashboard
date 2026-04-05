package com.finance.dashboard.repository;

import com.finance.dashboard.entity.enums.RecordType;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FinancialRecordRepository extends MongoRepository<FinancialRecord,String > {
    Page<FinancialRecord> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);

    Page<FinancialRecord> findByUserIdAndCategoryContainingIgnoreCaseAndIsDeletedFalse(String userId,String category, Pageable pageable);

    Page<FinancialRecord> findByUserIdAndRecordTypeAndIsDeletedFalse(String userId,RecordType type, Pageable pageable);

    Page<FinancialRecord>findByUserIdAndCategoryContainingIgnoreCaseAndRecordTypeAndIsDeletedFalse(String userId,String category,RecordType type ,Pageable pageable);
    Page<FinancialRecord> findByUserIdAndDateBetweenAndIsDeletedFalse(String userId,LocalDateTime start,LocalDateTime end, Pageable pageable);

}
