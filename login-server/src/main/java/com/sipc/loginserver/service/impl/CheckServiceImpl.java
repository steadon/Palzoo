package com.sipc.loginserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.loginserver.exception.BusinessException;
import com.sipc.loginserver.mapper.PermissionMapper;
import com.sipc.loginserver.mapper.UserMapper;
import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.Permission;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.LevelParam;
import com.sipc.loginserver.service.CheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CheckServiceImpl implements CheckService {
    @Resource
    UserMapper userMapper;
    @Resource
    PermissionMapper permissionMapper;

    @Override
    public CommonResult<LevelParam> checkRole(String openid) {
        //查询该用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("openid", openid)
                .eq("is_deleted", 0));

        if (user == null) throw new BusinessException("非法用户");
        //查询权限等级
        Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>()
                .eq("id", user.getPermissionId())
                .eq("is_deleted", 0));

        Integer level = permission.getLevel();
        String description = permission.getDescription();
        //包装后返回
        return CommonResult.success(new LevelParam(level, description));
    }
}
