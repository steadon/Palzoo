package com.sipc.loginserver.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.loginserver.exception.BusinessException;
import com.sipc.loginserver.mapper.UserMapper;
import com.sipc.loginserver.pojo.CommonResult;
import com.sipc.loginserver.pojo.domain.User;
import com.sipc.loginserver.pojo.param.DropUserInfoParam;
import com.sipc.loginserver.pojo.param.OpenIdParam;
import com.sipc.loginserver.pojo.param.PostNewUserIdParam;
import com.sipc.loginserver.pojo.param.SignInParam;
import com.sipc.loginserver.service.LoginService;
import com.sipc.loginserver.service.feign.UserServer;
import com.sipc.loginserver.util.WechatCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

/**
 * @author Sterben
 * 2023.4.4
 * @implNote 此注销删除为软删除
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private final UserMapper userMapper;
    private final UserServer userServer;

    @Autowired
    public LoginServiceImpl(UserMapper userMapper, UserServer userServer) {
        this.userMapper = userMapper;
        this.userServer = userServer;
    }

    /**
     * 注册并登录
     *
     * @param param code
     * @return openid
     */
    @Override
    @Transactional
    public CommonResult<OpenIdParam> signIn(SignInParam param) {
        //获取微信小程序相关常量
        String appid = WechatCommonUtil.APP_ID;
        String secret = WechatCommonUtil.APP_SECRET;
        String jsCode = param.getCode();
        log.info("code: " + jsCode);

        //拼接 url (此处使用工具类提高代码可读性)
        String url = UriComponentsBuilder.fromUriString("https://api.weixin.qq.com/sns/jscode2session").queryParam("appid", appid).queryParam("secret", secret).queryParam("js_code", param.getCode()).toUriString();

        //向微信服务器获取 openid 和 session
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        if (forObject == null) {
            throw new BusinessException("请求微信服务器失败");
        }
        //手动将 response 转 json
        String resp;
        resp = forObject.replaceAll("\\\\", "");
        JSONObject jsonResp = JSON.parseObject(resp);
        if (jsonResp == null) {
            throw new BusinessException("转化Json字符串出错");
        }

        //获取openid和session
        String openid = (String) jsonResp.get("openid");
        String sessionKey = (String) jsonResp.get("session_key");
        log.info("微信服务器响应: " + jsonResp);
        if (openid == null || sessionKey == null) {
            throw new BusinessException("无效的登录凭证");
        }

        //获取用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));

        //未注册增添加新用户
        if (user == null) {
            if (userMapper.insert(new User(openid)) == 0) {
                throw new BusinessException("添加用户失败");
            }

            //再次获取该用户(因为insert失败会抛出异常，所以此处采取乐观式检查)
            user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));

            //远程调用 user-server 添加用户
            String code = userServer.postNewUserInfo(new PostNewUserIdParam(user.getId(), user.getOpenid())).getCode();
            if (!Objects.equals(code, "00000")) throw new BusinessException("user-server新增用户异常");
        }
        return CommonResult.success(new OpenIdParam(sessionKey, openid));
    }

    /**
     * 注销用户
     *
     * @param openid openid
     * @return msg
     */
    @Override
    public CommonResult<String> signOff(String openid) {
        //查询用户验证合法性
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (user == null) throw new BusinessException("该用户不存在或已被删除");

        //远程调用 user-server 删除用户
        String code = userServer.dropUserInfo(new DropUserInfoParam(user.getId())).getCode();
        if (!Objects.equals(code, "00000")) throw new BusinessException("user-server删除用户异常");

        //软删除该用户
        user.setIsDeleted((byte) 1);
        if (userMapper.updateById(user) == 0) throw new BusinessException("注销失败");

        return CommonResult.success("注销成功");
    }
}
