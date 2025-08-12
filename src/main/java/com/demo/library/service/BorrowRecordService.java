package com.demo.library.service;

import com.demo.library.model.BorrowRecord;
import java.util.List;

public interface BorrowRecordService {
    List<BorrowRecord> findAll();
    BorrowRecord findById(Long id);
    BorrowRecord create(BorrowRecord record);
    BorrowRecord update(Long id, BorrowRecord record);
    boolean delete(Long id);
    List<BorrowRecord> findByUserId(Long userId);
}
