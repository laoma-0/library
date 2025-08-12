package com.demo.library.controller;

import com.demo.library.model.SystemConfig;
import com.demo.library.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system-configs")
public class SystemConfigController {
    @Autowired
    private SystemConfigService service;

    @GetMapping
    public Map<String, Object> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "all", required = false, defaultValue = "false") boolean all
    ) {
        Map<String, Object> map = new HashMap<>();
        if (all) {
            // 返回全部数据
            java.util.List<SystemConfig> allList = service.findAll();
            map.put("data", allList);
            map.put("total", allList.size());
            return map;
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<SystemConfig> result = service.findAll(keyword, pageable);
        map.put("data", result.getContent());
        map.put("total", result.getTotalElements());
        return map;
    }

    @GetMapping("/{id}")
    public SystemConfig getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/key/{configKey}")
    public SystemConfig getByKey(@PathVariable String configKey) {
        return service.findByConfigKey(configKey);
    }

    @PostMapping
    public SystemConfig create(@RequestBody SystemConfig config) {
        return service.create(config);
    }

    @PutMapping("/{id}")
    public SystemConfig update(@PathVariable Long id, @RequestBody SystemConfig config) {
        return service.update(id, config);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean result = service.delete(id);
        Map<String, Object> map = new HashMap<>();
        map.put("success", result);
        return map;
    }
}
