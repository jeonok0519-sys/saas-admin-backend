package com.saas.admin.dto;

import lombok.Data;

@Data
public class OperatorDTO {
    private Long id;
    private String username;
    private String realName;
    private String role;
    private Integer status;
}
