package com.saas.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.admin.entity.Tenant;
import com.saas.admin.mapper.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    public IPage<Tenant> page(int current, int size, String keyword, Integer status) {
        Page<Tenant> page = new Page<>(current, size);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Tenant::getTenantName, keyword)
                   .or()
                   .like(Tenant::getTenantCode, keyword);
        }
        if (status != null) {
            wrapper.eq(Tenant::getStatus, status);
        }
        wrapper.orderByDesc(Tenant::getCreateTime);
        return tenantMapper.selectPage(page, wrapper);
    }

    public Tenant findById(Long id) {
        return tenantMapper.selectById(id);
    }

    public Tenant findByTenantCode(String tenantCode) {
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getTenantCode, tenantCode);
        return tenantMapper.selectOne(wrapper);
    }

    public void save(Tenant tenant) {
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());
        tenantMapper.insert(tenant);
    }

    public void update(Tenant tenant) {
        tenant.setUpdateTime(LocalDateTime.now());
        tenantMapper.updateById(tenant);
    }

    public void delete(Long id) {
        tenantMapper.deleteById(id);
    }

    public void updateStatus(Long id, Integer status) {
        Tenant tenant = tenantMapper.selectById(id);
        tenant.setStatus(status);
        tenant.setUpdateTime(LocalDateTime.now());
        tenantMapper.updateById(tenant);
    }

    public Map<String, Object> getStatistics() {
        Long total = tenantMapper.selectCount(null);
        LambdaQueryWrapper<Tenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tenant::getStatus, 1);
        Long active = tenantMapper.selectCount(wrapper);
        return Map.of("total", total, "active", active);
    }
}
