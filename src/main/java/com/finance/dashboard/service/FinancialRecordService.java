package com.finance.dashboard.service;

import com.finance.dashboard.dto.FilterRequestDto;
import com.finance.dashboard.dto.FinancialRecordRequestDto;
import com.finance.dashboard.dto.FinancialRecordResponseDto;
import com.finance.dashboard.entity.model.FinancialRecord;
import com.finance.dashboard.exception.InvalidOperationException;
import com.finance.dashboard.exception.ResourceNotFoundExcption;
import com.finance.dashboard.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FinancialRecordService {
    private final ModelMapper modelMapper;
    private final FinancialRecordRepository recordRepository;
    public FinancialRecordResponseDto create(String userId, FinancialRecordRequestDto financialRecordRequestDto){
        FinancialRecord record=FinancialRecord.builder()
                .userId(userId)
                .description(financialRecordRequestDto.getDescription())
                .amount(financialRecordRequestDto.getAmount())
                .recordType(financialRecordRequestDto.getType())
                .category(financialRecordRequestDto.getCategory())
                .date(financialRecordRequestDto.getDate())
                .isDeleted(false)
                .build();
        recordRepository.save(record);
        return modelMapper.map(record, FinancialRecordResponseDto.class);
    }

    public Page<FinancialRecordResponseDto> getAllRecords(String userId, Pageable pageable){
        return recordRepository.findByUserIdAndIsDeletedFalse(userId,pageable).map(financialRecord ->modelMapper.map(financialRecord, FinancialRecordResponseDto.class) );

    }

    public FinancialRecordResponseDto update(String id,FinancialRecordRequestDto financialRecordRequestDto,String userId){
        FinancialRecord record=recordRepository.findById(id).orElseThrow(()-> new ResourceNotFoundExcption("Record not found"));
        if(!record.getUserId().equals(userId)){
            throw new InvalidOperationException("You are not allowed to update this record");
        }
        if(financialRecordRequestDto.getAmount()!=null){
            record.setAmount(financialRecordRequestDto.getAmount());
        }
        if(financialRecordRequestDto.getCategory()!=null){
            record.setCategory(financialRecordRequestDto.getCategory());
        }
        if(financialRecordRequestDto.getType()!=null){
            record.setRecordType(financialRecordRequestDto.getType());
        }
        if(financialRecordRequestDto.getDate()!=null){
            record.setDate(financialRecordRequestDto.getDate());
        }
        if(financialRecordRequestDto.getDescription()!=null){
            record.setDescription(financialRecordRequestDto.getDescription());
        }
        recordRepository.save(record);
        return modelMapper.map(record,FinancialRecordResponseDto.class);
    }
    public void delete(String id,String userId){
        FinancialRecord record= recordRepository.findById(id).orElseThrow(()-> new ResourceNotFoundExcption("Record not found"));
        if(!record.getUserId().equals(userId)){
            throw new InvalidOperationException("You are not allowed to delete this record !");
        }
        record.setDeleted(true);
        recordRepository.save(record);

    }
    public Page<FinancialRecordResponseDto> filter(FilterRequestDto filterRequestDto, Pageable pageable){
        if(filterRequestDto.getCategory()!=null && filterRequestDto.getType()!=null){
            return recordRepository.findByUserIdAndCategoryContainingIgnoreCaseAndRecordTypeAndIsDeletedFalse
                            (filterRequestDto.getUserId(),filterRequestDto.getCategory(),filterRequestDto.getType(),pageable)
                    .map(record->modelMapper.map(record,FinancialRecordResponseDto.class));
        }
        if(filterRequestDto.getCategory()!=null){
            return recordRepository.findByUserIdAndCategoryContainingIgnoreCaseAndIsDeletedFalse
                    (filterRequestDto.getUserId(),filterRequestDto.getCategory(),pageable)
            .map(record->modelMapper.map(record,FinancialRecordResponseDto.class));
        }

        if(filterRequestDto.getType()!=null){
            return recordRepository.findByUserIdAndRecordTypeAndIsDeletedFalse
                    (filterRequestDto.getUserId(),filterRequestDto.getType(),pageable)
                    .map(record -> modelMapper.map(record,FinancialRecordResponseDto.class));
        }
        if (filterRequestDto.getStart()!=null && filterRequestDto.getEnd()!=null){

            return recordRepository.findByUserIdAndDateBetweenAndIsDeletedFalse
                            (filterRequestDto.getUserId(),filterRequestDto.getStart(),filterRequestDto.getEnd(),pageable)
                    .map(record -> modelMapper.map(record,FinancialRecordResponseDto.class));
        }
        return getAllRecords(filterRequestDto.getUserId(), pageable);


    }
}
