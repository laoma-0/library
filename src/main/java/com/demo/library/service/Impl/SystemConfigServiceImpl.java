package com.demo.library.service.Impl;

import com.demo.library.model.SystemConfig;
import com.demo.library.repository.SystemConfigRepository;
import com.demo.library.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {
    @Autowired
    private SystemConfigRepository repository;

    @Override
    public Page<SystemConfig> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable);
        }
        // 需要引入JpaSpecificationExecutor支持，且Repository需继承JpaSpecificationExecutor
        return repository.findAll(new org.springframework.data.jpa.domain.Specification<SystemConfig>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(javax.persistence.criteria.Root<SystemConfig> root,
                                                                   javax.persistence.criteria.CriteriaQuery<?> query,
                                                                   javax.persistence.criteria.CriteriaBuilder cb) {
                String k = "%" + keyword.trim() + "%";
                return cb.or(
                    cb.like(root.get("configKey"), k),
                    cb.like(root.get("description"), k)
                );
            }
        }, pageable);
    }

    @Override
    public List<SystemConfig> findAll() {
        return repository.findAll();
    }

    @Override
    public SystemConfig findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public SystemConfig findByConfigKey(String configKey) {
        return repository.findByConfigKey(configKey).orElse(null);
    }

    @Override
    public SystemConfig create(SystemConfig config) {
        if (repository.existsByConfigKey(config.getConfigKey())) {
            throw new RuntimeException("配置键已存在");
        }
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        return repository.save(config);
    }

    @Override
    public SystemConfig update(Long id, SystemConfig config) {
        SystemConfig exist = findById(id);
        if (exist == null) throw new RuntimeException("配置不存在");
        exist.setConfigValue(config.getConfigValue());
        exist.setConfigType(config.getConfigType());
        exist.setDescription(config.getDescription());
        exist.setIsSystem(config.getIsSystem());
        exist.setUpdateTime(LocalDateTime.now());
        return repository.save(exist);
    }

    @Override
    public boolean delete(Long id) {
        SystemConfig exist = findById(id);
        if (exist == null) return false;
        if (exist.getIsSystem() != null && exist.getIsSystem() == 1) {
            throw new RuntimeException("系统内置配置不可删除");
        }
        repository.deleteById(id);
        return true;
    }
}
