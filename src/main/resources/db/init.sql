CREATE DATABASE IF NOT EXISTS saas_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE saas_admin;

CREATE TABLE IF NOT EXISTS sys_tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_name VARCHAR(100) NOT NULL COMMENT '租户名称',
    tenant_code VARCHAR(50) NOT NULL UNIQUE COMMENT '租户编码',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=禁用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_code (tenant_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

CREATE TABLE IF NOT EXISTS sys_operator (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(20) NOT NULL COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL COMMENT '角色：SUPER_ADMIN=超级管理员，OPERATOR=普通运营',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作员表';

INSERT INTO sys_operator (username, password, real_name, role, status) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '超级管理员', 'SUPER_ADMIN', 1);

INSERT INTO sys_tenant (tenant_name, tenant_code, status, remark) VALUES
('示例科技有限公司', 'example_tech', 1, '这是一个示例租户'),
('测试公司', 'test_company', 1, '用于测试的租户');
