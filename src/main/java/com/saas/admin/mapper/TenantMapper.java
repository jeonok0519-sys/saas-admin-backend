package com.saas.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saas.admin.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
