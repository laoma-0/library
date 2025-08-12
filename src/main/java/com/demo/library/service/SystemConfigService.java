package com.demo.library.service;

import com.demo.library.model.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SystemConfigService {
    Page<SystemConfig> findAll(String keyword, Pageable pageable);
    SystemConfig findById(Long id);
    SystemConfig findByConfigKey(String configKey);
    SystemConfig create(SystemConfig config);
    SystemConfig update(Long id, SystemConfig config);
    boolean delete(Long id);
    List<SystemConfig> findAll();
}
