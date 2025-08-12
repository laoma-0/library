package com.demo.library.controller;

import com.demo.library.model.BorrowRecord;
import com.demo.library.model.User;
import com.demo.library.model.Book;
import com.demo.library.service.BorrowRecordService;
import com.demo.library.service.UserService;
import com.demo.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordController {
    @Autowired
    private BorrowRecordService borrowRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "bookId", required = false) Long bookId,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        // 权限校验：如果userId不为空，且该用户是读者，只能查自己的借阅记录
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户不存在");
            }
            if (user.getRole() == 2) { // 读者
                List<BorrowRecord> records = borrowRecordService.findByUserId(userId);
                // 新增：前端可传bookId/status进行过滤
                if (bookId != null) {
                    records.removeIf(r -> !bookId.equals(r.getBookId()));
                }
                if (status != null) {
                    records.removeIf(r -> !status.equals(r.getStatus()));
                }
                // ...组装返回数据（与原有逻辑一致）...
                List<User> users = userService.findAll(null);
                List<Book> books = bookService.findAll();
                Map<Long, User> userMap = new HashMap<>();
                Map<Long, Book> bookMap = new HashMap<>();
                for (User u : users) userMap.put(u.getId(), u);
                for (Book b : books) bookMap.put(b.getId(), b);
                List<Map<String, Object>> list = new ArrayList<>();
                for (BorrowRecord r : records) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", r.getId());
                    map.put("userId", r.getUserId());
                    map.put("userName", userMap.get(r.getUserId()) != null ? userMap.get(r.getUserId()).getRealName() : "");
                    map.put("bookId", r.getBookId());
                    map.put("bookTitle", bookMap.get(r.getBookId()) != null ? bookMap.get(r.getBookId()).getTitle() : "");
                    map.put("borrowDate", r.getBorrowDate());
                    map.put("returnDate", r.getReturnDate());
                    map.put("status", r.getStatus());
                    map.put("createTime", r.getCreateTime());
                    list.add(map);
                }
                Map<String, Object> result = new HashMap<>();
                result.put("data", list);
                result.put("total", list.size());
                return ResponseEntity.ok(result);
            }
        }
        // 管理员或未传userId，返回全部
        List<BorrowRecord> records = borrowRecordService.findAll();
        // 新增：前端可传bookId/status进行过滤
        if (bookId != null) {
            records.removeIf(r -> !bookId.equals(r.getBookId()));
        }
        if (status != null) {
            records.removeIf(r -> !status.equals(r.getStatus()));
        }
        List<User> users = userService.findAll(null);
        List<Book> books = bookService.findAll();
        Map<Long, User> userMap = new HashMap<>();
        Map<Long, Book> bookMap = new HashMap<>();
        for (User u : users) userMap.put(u.getId(), u);
        for (Book b : books) bookMap.put(b.getId(), b);
        List<Map<String, Object>> list = new ArrayList<>();
        for (BorrowRecord r : records) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("userId", r.getUserId());
            map.put("userName", userMap.get(r.getUserId()) != null ? userMap.get(r.getUserId()).getRealName() : "");
            map.put("bookId", r.getBookId());
            map.put("bookTitle", bookMap.get(r.getBookId()) != null ? bookMap.get(r.getBookId()).getTitle() : "");
            map.put("borrowDate", r.getBorrowDate());
            map.put("returnDate", r.getReturnDate());
            map.put("status", r.getStatus());
            map.put("createTime", r.getCreateTime());
            list.add(map);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", list);
        result.put("total", list.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public BorrowRecord getById(@PathVariable Long id) {
        return borrowRecordService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BorrowRecord record, @RequestParam(value = "userId") Long userId) {
        // 权限校验：只有管理员或本人可新增
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户不存在");
        }
        if (user.getRole() == 2 && !user.getId().equals(record.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("读者只能为自己添加借阅记录");
        }
        BorrowRecord created = borrowRecordService.create(record);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BorrowRecord record, @RequestParam(value = "userId") Long userId) {
        // 权限校验：只有管理员或本人可修改
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户不存在");
        }
        if (user.getRole() == 2 && !user.getId().equals(record.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("读者只能修改自己的借阅记录");
        }
        BorrowRecord updated = borrowRecordService.update(id, record);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {
        // 权限校验：只有管理员或本人可删除
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户不存在");
        }
        BorrowRecord record = borrowRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("借阅记录不存在");
        }
        if (user.getRole() == 2 && !user.getId().equals(record.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("读者只能删除自己的借阅记录");
        }
        boolean result = borrowRecordService.delete(id);
        if (!result) {
            // 删除失败时返回 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败，可能记录不存在或已被删除");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", result);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<?> batchDelete(@RequestBody Map<String, List<Long>> body, @RequestParam(value = "userId", required = false) Long userId) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("未传递要删除的ID列表");
        }
        // 去除管理员权限校验，所有用户均可批量删除
        int success = 0;
        List<Long> failedIds = new ArrayList<>();
        for (Long id : ids) {
            boolean deleted = borrowRecordService.delete(id);
            if (deleted) {
                success++;
            } else {
                failedIds.add(id);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", failedIds.isEmpty());
        result.put("deleted", success);
        result.put("failedIds", failedIds);
        return ResponseEntity.ok(result);
    }
}
