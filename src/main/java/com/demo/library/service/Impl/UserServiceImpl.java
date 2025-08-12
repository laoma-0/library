package com.demo.library.service.Impl;

import com.demo.library.model.User;
import com.demo.library.repository.UserRepository;
import com.demo.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initDefaultAdmin() {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRealName("系统管理员");
            admin.setRole(1); // 管理员
            admin.setStatus(1); // 启用
            this.createUser(admin);
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(User user) {
        // 检查用户名唯一
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        // 其余字段直接保存
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setRealName(userDetails.getRealName());
            user.setPhone(userDetails.getPhone());
            user.setEmail(userDetails.getEmail());
            user.setStatus(userDetails.getStatus());
            user.setRole(userDetails.getRole());
            user.setUpdateTime(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAll(Specification<User> spec) {
        return userRepository.findAll(spec);
    }
}