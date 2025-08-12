package com.demo.library.controller;

import com.demo.library.model.User;
import com.demo.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Map<String, Object> getAllUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "phone", required = false) String phone
    ) {
        // 动态条件查询
        Specification<User> spec = (root, query, cb) -> {
            // 构建条件
            javax.persistence.criteria.Predicate predicate = cb.conjunction();
            if (StringUtils.hasText(username)) {
                predicate = cb.and(predicate, cb.like(root.get("username"), "%" + username + "%"));
            }
            if (StringUtils.hasText(phone)) {
                predicate = cb.and(predicate, cb.like(root.get("phone"), "%" + phone + "%"));
            }
            return predicate;
        };
        List<User> users = userService.findAll(spec);
        Map<String, Object> result = new HashMap<>();
        result.put("data", users); // 必须为用户数组
        result.put("total", users.size());
        return result;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            // 校验用户名唯一
            if (userService.getUserByUsername(user.getUsername()) != null) {
                throw new RuntimeException("用户名已存在");
            }
            // 校验真实姓名
            if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
                throw new RuntimeException("真实姓名不能为空");
            }
            // 类型安全校验，防止role为字符串时报错
            if (user.getRole() != null && !(user.getRole() == 1 || user.getRole() == 2)) {
                throw new RuntimeException("角色参数不合法，只能为1(管理员)或2(读者)");
            }
            // 设置默认角色、状态、创建时间
            if (user.getRole() == null) user.setRole(2); // 普通用户
            if (user.getStatus() == null) user.setStatus(1); // 正常
            if (user.getCreateTime() == null) user.setCreateTime(java.time.LocalDateTime.now());
            return userService.createUser(user);
        } catch (Exception e) {
            logger.error("创建用户时出错", e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> loginUser) {
        String username = (String) loginUser.get("username");
        String password = (String) loginUser.get("password");
        String role = (String) loginUser.get("role");
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 1=管理员, 2=读者
            boolean isAdmin = (user.getRole() != null && user.getRole() == 1);
            boolean isReader = (user.getRole() != null && user.getRole() == 2);
            boolean roleMatch = ("admin".equals(role) && isAdmin) || ("user".equals(role) && isReader);
            if (roleMatch) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("user", user);
                response.put("role", role);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "身份不匹配或无权限");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @DeleteMapping("/batch")
    public Map<String, Object> batchDeleteUsers(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        int success = 0;
        int fail = 0;
        for (Long id : ids) {
            boolean result = userService.deleteUser(id);
            if (result) success++;
            else fail++;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("fail", fail);
        return result;
    }

    // 用户状态变更（禁用/启用）接口
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Integer status = null;
            Object statusObj = body.get("status");
            if (statusObj instanceof Integer) {
                status = (Integer) statusObj;
            } else if (statusObj instanceof String) {
                // 支持前端传递 "正常"/"禁用" 或 "1"/"0"
                if ("正常".equals(statusObj)) status = 1;
                else if ("禁用".equals(statusObj)) status = 0;
                else {
                    try { status = Integer.parseInt(statusObj.toString()); } catch (Exception ignore) {}
                }
            }
            if (status == null || (status != 0 && status != 1)) {
                return ResponseEntity.badRequest().body("无效的状态参数");
            }
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
            }
            user.setStatus(status);
            userService.updateUser(id, user);
            return ResponseEntity.ok("状态更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("状态更新失败: " + e.getMessage());
        }
    }

    // 注册接口：仅允许注册读者（role=2）
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            String realName = (String) body.get("realName");
            String phone = (String) body.get("phone");
            String email = (String) body.get("email");
            // 前端role为"user"，后端统一为2
            Integer role = 2;
            if (username == null || username.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "账号不能为空");
                return result;
            }
            if (password == null || password.length() < 6) {
                result.put("success", false);
                result.put("message", "密码长度不能少于6位");
                return result;
            }
            if (realName == null || realName.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "姓名不能为空");
                return result;
            }
            if (phone == null || phone.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "手机号不能为空");
                return result;
            }
            if (email == null || email.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "邮箱不能为空");
                return result;
            }
            if (userService.getUserByUsername(username) != null) {
                result.put("success", false);
                result.put("message", "账号已存在");
                return result;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRealName(realName);
            user.setPhone(phone);
            user.setEmail(email);
            user.setRole(role); // 只允许注册读者
            user.setStatus(1);
            user.setCreateTime(java.time.LocalDateTime.now());
            userService.createUser(user);
            result.put("success", true);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败: " + e.getMessage());
        }
        return result;
    }
}