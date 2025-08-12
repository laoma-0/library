package com.demo.library.controller;

import com.demo.library.model.Book;
import com.demo.library.model.Category;
import com.demo.library.service.BookService;
import com.demo.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 图书控制器类，处理与图书相关的HTTP请求。
 * 包括获取图书列表、创建、更新、删除图书等操作。
 */
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有图书信息，并支持多条件过滤。
     * 
     * @param title  图书标题（可选）
     * @param author 作者（可选）
     * @param isbn   ISBN（可选）
     * @param status 状态（可选）
     * @return 符合条件的图书列表
     */
    @GetMapping
    public List<Book> getAllBooks(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "author", required = false) String author,
        @RequestParam(value = "isbn", required = false) String isbn,
        @RequestParam(value = "status", required = false) Integer status
    ) {
        List<Book> books = bookService.findAll();
        if (title != null && !title.isEmpty()) {
            books.removeIf(b -> b.getTitle() == null || !b.getTitle().contains(title));
        }
        if (author != null && !author.isEmpty()) {
            books.removeIf(b -> b.getAuthor() == null || !b.getAuthor().contains(author));
        }
        if (isbn != null && !isbn.isEmpty()) {
            books.removeIf(b -> b.getIsbn() == null || !b.getIsbn().contains(isbn));
        }
        if (status != null) {
            books.removeIf(b -> !status.equals(b.getStatus()));
        }
        return books;
    }

    /**
     * 根据图书ID获取单个图书信息。
     * 
     * @param id 图书ID
     * @return 对应的图书对象
     */
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    /**
     * 创建新的图书记录。
     * 
     * @param book 包含图书信息的实体对象
     * @return 创建成功的图书对象
     */
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            // 校验 categoryId 是否有效
            if (book.getCategoryId() == null || !categoryService.existsById(book.getCategoryId())) {
                throw new RuntimeException("无效的分类ID");
            }
            Book savedBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            // 增强异常处理，返回友好的错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 500);
            errorResponse.put("message", "服务器内部错误：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 更新指定ID的图书信息。
     * 
     * @param id   图书ID
     * @param book 包含更新信息的实体对象
     * @return 更新后的图书对象
     */
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    /**
     * 删除指定ID的图书记录。
     * 
     * @param id 图书ID
     * @return 删除结果（成功或失败）
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBook(@PathVariable Long id) {
        boolean result = bookService.deleteBook(id);
        Map<String, Object> map = new HashMap<>();
        map.put("success", result);
        return map;
    }

    /**
     * 批量删除图书记录。
     * 
     * @param requestBody 包含要删除的图书ID列表的请求体
     * @return 删除结果（成功或失败）
     */
    @PostMapping("/delete-batch") // 确保路径为 /delete-batch
    public ResponseEntity<?> deleteBooksBatch(@RequestBody Map<String, List<Long>> requestBody) {
        try {
            // 检查请求体是否为空或不包含 ids 字段
            if (requestBody == null || !requestBody.containsKey("ids")) {
                return ResponseEntity.badRequest().body("IDs list is missing in the request body");
            }

            List<Long> ids = requestBody.get("ids");
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.badRequest().body("IDs list is empty");
            }

            // 调用服务层方法删除图书
            bookService.deleteBooks(ids);
            return ResponseEntity.ok("Books deleted successfully");
        } catch (Exception e) {
            // 增强异常处理，返回详细的错误信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 500);
            errorResponse.put("message", "Error deleting books: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 获取所有图书信息，并包含分类名称。
     * 
     * @return 包含图书信息和分类名称的响应
     */
    @GetMapping("/with-category")
    public ResponseEntity<?> getAllBooksWithCategory() {
        try {
            List<Book> books = bookService.findAll();
            List<Category> categories = categoryService.findAll();
            Map<Long, String> categoryMap = new HashMap<>();
            for (Category c : categories) {
                categoryMap.put(c.getId(), c.getName());
            }
            List<Map<String, Object>> bookList = new ArrayList<>();
            for (Book b : books) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", b.getId());
                map.put("title", b.getTitle());
                map.put("author", b.getAuthor());
                map.put("isbn", b.getIsbn());
                map.put("categoryId", b.getCategoryId());
                map.put("categoryName", categoryMap.getOrDefault(b.getCategoryId(), ""));
                map.put("publisher", b.getPublisher());
                map.put("publishDate", b.getPublishDate());
                map.put("price", b.getPrice());
                map.put("description", b.getDescription());
                map.put("status", b.getStatus());
                map.put("createTime", b.getCreateTime());
                map.put("updateTime", b.getUpdateTime());
                bookList.add(map);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("data", bookList);
            result.put("total", bookList.size());
            return ResponseEntity.ok(result); // 返回成功的响应
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈信息
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch books with categories");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);// 返回友好的错误信息
        }
    }

    /**
     * 搜索图书，支持按标题、作者和ISBN进行模糊查询。
     * 
     * @param title  图书标题（可选）
     * @param author 作者（可选）
     * @param isbn   ISBN（可选）
     * @return 符合条件的图书列表
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "isbn", required = false) String isbn) {
        // 构建查询条件
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (author != null) {
                predicates.add(criteriaBuilder.like(root.get("author"), "%" + author + "%"));
            }
            if (isbn != null) {
                predicates.add(criteriaBuilder.like(root.get("isbn"), "%" + isbn + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 查询图书列表
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }
}
