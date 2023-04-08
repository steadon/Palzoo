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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 鉴权&&获取用户对象
 *
 * @author Sterben
 */
@Slf4j
@Service
public class CheckServiceImpl implements CheckService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public CommonResult<LevelParam> checkRole(String openid) {
        //查询该用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (user == null) throw new BusinessException("非法用户");

        //查询权限等级
        Permission permission = permissionMapper.selectOne(new QueryWrapper<Permission>().eq("id", user.getPermissionId()));
        if (permission == null) throw new BusinessException("非法用户");

        //包装后返回
        return CommonResult.success(new LevelParam(permission.getLevel(), permission.getDescription()));
    }

    /**
     * 通过 openid 获取 user 对象
     *
     * @param openid 微信用户唯一标识
     * @return 查询到的 user 对象
     */
    @Override
    public User checkUser(String openid) {
        log.info("openid: " + openid);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (user == null) throw new BusinessException("非法用户");
        return user;
    }
}
