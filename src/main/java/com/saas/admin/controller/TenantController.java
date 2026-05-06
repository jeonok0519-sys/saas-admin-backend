package com.saas.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.admin.common.R;
import com.saas.admin.dto.PageResponse;
import com.saas.admin.entity.Tenant;
import com.saas.admin.service.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping
    public R<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        try {
            String role = (String) request.getAttribute("role");
            System.out.println("Role: " + role);
            System.out.println("Current: " + current + ", Size: " + size + ", Keyword: " + keyword + ", Status: " + status);
            
            IPage<Tenant> page = tenantService.page(current, size, keyword, status);
            System.out.println("Page total: " + page.getTotal());
            System.out.println("Page records size: " + page.getRecords().size());
            
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("records", page.getRecords());
            result.put("total", page.getTotal());
            result.put("size", page.getSize());
            result.put("current", page.getCurrent());
            result.put("pages", page.getPages());
            
            System.out.println("Result created successfully, about to return");
            return R.success(result);
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
            return R.error("服务器错误");
        }
    }

    @GetMapping("/{id}")
    public R<Tenant> getById(@PathVariable Long id) {
        Tenant tenant = tenantService.findById(id);
        return R.success(tenant);
    }

    @PostMapping
    public R<Void> create(@RequestBody Tenant tenant, HttpServletRequest request) {
        System.out.println("收到创建租户请求: " + tenant);
        
        String role = (String) request.getAttribute("role");
        System.out.println("用户角色: " + role);
        
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        Tenant existing = tenantService.findByTenantCode(tenant.getTenantCode());
        if (existing != null) {
            return R.error("租户编码已存在");
        }

        tenantService.save(tenant);
        System.out.println("租户创建成功");
        return R.success();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Tenant tenant, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        tenant.setId(id);
        tenantService.update(tenant);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        tenantService.delete(id);
        return R.success();
    }

    @PutMapping("/{id}/status")
    public R<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body,
            HttpServletRequest request) {

        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        Integer status = body.get("status");
        tenantService.updateStatus(id, status);
        return R.success();
    }

    @GetMapping("/statistics")
    public R<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = tenantService.getStatistics();
        return R.success(statistics);
    }
}
