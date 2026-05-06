package com.saas.admin.dto;

import lombok.Data;

@Data
public class TenantDTO {
    private Long id;
    private String tenantName;
    private String tenantCode;
    private Integer status;
    private String remark;
}
