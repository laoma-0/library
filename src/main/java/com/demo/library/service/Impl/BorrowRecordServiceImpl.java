package com.demo.library.service.Impl;

import com.demo.library.model.BorrowRecord;
import com.demo.library.model.Book;
import com.demo.library.repository.BorrowRecordRepository;
import com.demo.library.service.BookService;
import com.demo.library.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookService bookService; // 新增

    @Override
    public List<BorrowRecord> findAll() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public BorrowRecord findById(Long id) {
        return borrowRecordRepository.findById(id).orElse(null);
    }

    @Override
    public BorrowRecord create(BorrowRecord record) {
        record.setCreateTime(LocalDateTime.now());
        if (record.getBorrowDate() == null) {
            record.setBorrowDate(LocalDateTime.now());
        }
        // 新增：借出时同步修改图书状态
        if (record.getBookId() != null) {
            Book book = bookService.findById(record.getBookId());
            if (book != null) {
                // 0-已借出，1-可借阅
                if (record.getStatus() != null && record.getStatus() == 0) {
                    book.setStatus(0);
                } else if (record.getStatus() != null && record.getStatus() == 1) {
                    book.setStatus(1);
                }
                bookService.updateBook(book.getId(), book);
            }
        }
        return borrowRecordRepository.save(record);
    }

    @Override
    public BorrowRecord update(Long id, BorrowRecord record) {
        Optional<BorrowRecord> optional = borrowRecordRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("借阅记录不存在");
        }
        BorrowRecord exist = optional.get();
        exist.setUserId(record.getUserId());
        exist.setBookId(record.getBookId());
        exist.setBorrowDate(record.getBorrowDate());
        exist.setReturnDate(record.getReturnDate());
        exist.setStatus(record.getStatus());
        // 新增：同步修改图书状态
        if (record.getBookId() != null) {
            Book book = bookService.findById(record.getBookId());
            if (book != null) {
                if (record.getStatus() != null && record.getStatus() == 0) {
                    book.setStatus(0);
                } else if (record.getStatus() != null && record.getStatus() == 1) {
                    book.setStatus(1);
                }
                bookService.updateBook(book.getId(), book);
            }
        }
        return borrowRecordRepository.save(exist);
    }

    @Override
    public boolean delete(Long id) {
        if (!borrowRecordRepository.existsById(id)) return false;
        borrowRecordRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BorrowRecord> findByUserId(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }
}
