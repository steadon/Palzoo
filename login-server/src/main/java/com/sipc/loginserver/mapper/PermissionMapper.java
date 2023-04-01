package com.sipc.loginserver.mapper;

import com.sipc.loginserver.pojo.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sterben
 * @since 2023-04-02
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}
