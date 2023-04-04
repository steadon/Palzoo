package com.sipc.loginserver.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.loginserver.exception.BusinessException;
import com.sipc.loginserver.mapper.UserMapper;
import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.OpenIdParam;
import com.sipc.loginserver.pojo.param.SignInParam;
import com.sipc.loginserver.service.LoginService;
import com.sipc.loginserver.util.WechatCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

/**
 * @author Sterben
 * 2023.4.4
 * @implNote 微信登录&&软删除注销
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private WechatCommonUtil wechatCommonUtil;
    @Resource
    private UserMapper userMapper;

    @Override
//    @SneakyThrows
    @Transactional
    public CommonResult<OpenIdParam> signIn(SignInParam param) {
        //获取微信小程序相关常量
        String appid = wechatCommonUtil.getAppId();
        String secret = wechatCommonUtil.getAppSecret();
        String jsCode = param.getCode();
        log.info("code:" + jsCode);

        //向微信服务器获取openid和session
        String url = UriComponentsBuilder.fromUriString("https://api.weixin.qq.com/sns/jscode2session")
                .queryParam("appid", appid)
                .queryParam("secret", secret)
                .queryParam("js_code", param.getCode())
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);

        //response转json
        String s = null;
        if (forObject != null) s = forObject.replaceAll("\\\\", "");
        JSONObject jsonObject = JSON.parseObject(s);

        //获取openid和session
        if (jsonObject == null) {
            throw new BusinessException("请求微信服务器失败");
        }
        String openid = (String) jsonObject.get("openid");
        String sessionKey = (String) jsonObject.get("session_key");
        log.info(String.valueOf(jsonObject));
        if (openid == null || sessionKey == null) {
            throw new BusinessException("无效的登录凭证");
        }

        //获取信息
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("openid", openid)
                .eq("is_deleted", 0));

        //未注册增添加新用户
        if (user == null) {
            if (userMapper.insert(new User(openid)) == 0) {
                throw new BusinessException("添加用户失败");
            }
        }
        return CommonResult.success(new OpenIdParam(sessionKey, openid));
    }

    @Override
    public CommonResult<String> signOff(String openid) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("openid", openid)
                .eq("is_deleted", 0));

        if (user == null) throw new BusinessException("该用户不存在或已被删除");
        user.setIsDeleted((byte) 1);
        if (userMapper.updateById(user) == 0) throw new BusinessException("注销失败");
        return CommonResult.success("注销成功");
    }
}
