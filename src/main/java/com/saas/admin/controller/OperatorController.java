package com.saas.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.saas.admin.common.R;
import com.saas.admin.entity.Operator;
import com.saas.admin.service.OperatorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @GetMapping
    public R<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request) {
        try {
            String role = (String) request.getAttribute("role");
            System.out.println("Role: " + role);
            System.out.println("Current: " + current + ", Size: " + size + ", Keyword: " + keyword);

            if (!"SUPER_ADMIN".equals(role)) {
                throw new AccessDeniedException("无权限访问");
            }

            IPage<Operator> page = operatorService.page(current, size, keyword);
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
    public R<Operator> getById(@PathVariable Long id) {
        Operator operator = operatorService.findById(id);
        return R.success(operator);
    }

    @PostMapping
    public R<Void> create(@RequestBody Operator operator, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        Operator existing = operatorService.findByUsername(operator.getUsername());
        if (existing != null) {
            return R.error("账号已存在");
        }

        operatorService.save(operator);
        return R.success();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Operator operator, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        Operator existing = operatorService.findById(id);
        if (existing != null && "SUPER_ADMIN".equals(existing.getRole()) && !"SUPER_ADMIN".equals(operator.getRole())) {
            return R.error("不能修改超级管理员的角色");
        }

        operator.setId(id);
        operator.setPassword(null);
        operatorService.update(operator);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        Operator operator = operatorService.findById(id);
        if (operator != null && "SUPER_ADMIN".equals(operator.getRole())) {
            return R.error("不能删除超级管理员");
        }

        Long currentUserId = (Long) request.getAttribute("userId");
        if (id.equals(currentUserId)) {
            return R.error("不能删除当前登录账号");
        }

        operatorService.delete(id);
        return R.success();
    }

    @PostMapping("/{id}/reset-password")
    public R<Void> resetPassword(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"SUPER_ADMIN".equals(role)) {
            throw new AccessDeniedException("无权限访问");
        }

        operatorService.resetPassword(id);
        return R.success();
    }
}
