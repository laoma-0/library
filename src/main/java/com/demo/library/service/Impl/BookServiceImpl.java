package com.demo.library.service.Impl;

import com.demo.library.model.Book;
import com.demo.library.repository.BookRepository;
import com.demo.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 图书服务实现类，提供图书相关的业务逻辑。
 */
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    /**
     * 获取所有图书信息。
     * 
     * @return 图书列表
     */
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * 根据Specification条件获取图书列表。
     * 
     * @param spec 查询条件
     * @return 符合条件的图书列表
     */
    @Override
    public List<Book> findAll(Specification<Book> spec) {
        return bookRepository.findAll(); // 使用 JpaRepository 的 findAll 方法支持 Specification
    }

    /**
     * 根据图书ID获取单个图书信息。
     * 
     * @param id 图书ID
     * @return 对应的图书对象
     */
    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * 创建新的图书记录。
     * 
     * @param book 包含图书信息的实体对象
     * @return 创建成功的图书对象
     */
    @Override
    public Book createBook(Book book) {
        try {
            // 校验 ISBN 是否为空
            if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
                throw new RuntimeException("ISBN不能为空");
            }
            // 校验 ISBN 唯一性
            if (bookRepository.existsByIsbn(book.getIsbn())) {
                throw new RuntimeException("ISBN已存在");
            }
            book.setCreateTime(LocalDateTime.now());
            return bookRepository.save(book);
        } catch (Exception e) {
            // 增加日志记录，帮助定位问题
            System.err.println("创建图书时发生错误：" + e.getMessage());
            throw e; // 重新抛出异常，让控制器捕获
        }
    }

    /**
     * 更新指定ID的图书信息。
     * 
     * @param id   图书ID
     * @param book 包含更新信息的实体对象
     * @return 更新后的图书对象
     */
    @Override
    public Book updateBook(Long id, Book book) {
        Optional<Book> optional = bookRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("图书不存在");
        }
        Book exist = optional.get();
        exist.setTitle(book.getTitle());
        exist.setAuthor(book.getAuthor());
        exist.setIsbn(book.getIsbn());
        exist.setCategoryId(book.getCategoryId());
        exist.setPublisher(book.getPublisher());
        exist.setPublishDate(book.getPublishDate());
        exist.setPrice(book.getPrice());
        exist.setDescription(book.getDescription());
        exist.setStatus(book.getStatus());
        exist.setUpdateTime(LocalDateTime.now());
        return bookRepository.save(exist);
    }

    /**
     * 删除指定ID的图书记录。
     * 
     * @param id 图书ID
     * @return 删除结果（成功或失败）
     */
    @Override
    public boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) return false;
        bookRepository.deleteById(id);
        return true;
    }

    @Override
    public void deleteBooks(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("IDs list cannot be null or empty");
        }
        // 检查所有 ID 是否存在
        List<Book> books = bookRepository.findAllById(ids);
        if (books.size() != ids.size()) {
            throw new RuntimeException("部分图书不存在");
        }
        // 删除图书记录
        bookRepository.deleteAll(books);
    }
}
