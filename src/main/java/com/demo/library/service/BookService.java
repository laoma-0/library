package com.demo.library.service;

import com.demo.library.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    List<Book> findAll(Specification<Book> spec);
    Book findById(Long id);
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    boolean deleteBook(Long id);

    // 新增: 批量删除图书的方法
    void deleteBooks(List<Long> ids);
}
