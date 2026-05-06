package com.saas.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saas.admin.entity.Operator;
import com.saas.admin.mapper.OperatorMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OperatorService {

    @Autowired
    private OperatorMapper operatorMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Operator findByUsername(String username) {
        LambdaQueryWrapper<Operator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Operator::getUsername, username);
        return operatorMapper.selectOne(wrapper);
    }

    public Operator findById(Long id) {
        return operatorMapper.selectById(id);
    }

    public IPage<Operator> page(int current, int size, String keyword) {
        Page<Operator> page = new Page<>(current, size);
        LambdaQueryWrapper<Operator> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Operator::getUsername, keyword)
                   .or()
                   .like(Operator::getRealName, keyword);
        }
        wrapper.orderByDesc(Operator::getCreateTime);
        IPage<Operator> result = operatorMapper.selectPage(page, wrapper);
        result.getRecords().forEach(op -> op.setPassword(null));
        return result;
    }

    public void save(Operator operator) {
        operator.setPassword(passwordEncoder.encode(operator.getPassword()));
        operator.setCreateTime(LocalDateTime.now());
        operator.setUpdateTime(LocalDateTime.now());
        operatorMapper.insert(operator);
    }

    public void update(Operator operator) {
        operator.setUpdateTime(LocalDateTime.now());
        operatorMapper.updateById(operator);
    }

    public void delete(Long id) {
        operatorMapper.deleteById(id);
    }

    public void resetPassword(Long id) {
        Operator operator = operatorMapper.selectById(id);
        operator.setPassword(passwordEncoder.encode("123456"));
        operator.setUpdateTime(LocalDateTime.now());
        operatorMapper.updateById(operator);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        Operator operator = operatorMapper.selectById(id);
        if (!passwordEncoder.matches(oldPassword, operator.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        operator.setPassword(passwordEncoder.encode(newPassword));
        operator.setUpdateTime(LocalDateTime.now());
        operatorMapper.updateById(operator);
    }

    public boolean isSuperAdmin(Long id) {
        Operator operator = operatorMapper.selectById(id);
        return operator != null && "SUPER_ADMIN".equals(operator.getRole());
    }
}
