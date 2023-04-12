package com.sipc.userserver.service.impl;

import com.sipc.userserver.config.MinioConfig;
import com.sipc.userserver.mapper.AcaMajorMapper;
import com.sipc.userserver.mapper.UserInfoMapper;
import com.sipc.userserver.pojo.CommonResult;
import com.sipc.userserver.pojo.domain.AcaMajor;
import com.sipc.userserver.pojo.domain.UserInfo;
import com.sipc.userserver.pojo.param.DropUserInfoParam;
import com.sipc.userserver.pojo.param.PostNewUserIdParam;
import com.sipc.userserver.pojo.param.UpdateUserAvatarParam;
import com.sipc.userserver.pojo.param.UpdateUserInfoParam;
import com.sipc.userserver.pojo.result.GetUserInfoResult;
import com.sipc.userserver.service.UserInfoService;
import com.sipc.userserver.util.MinioUtil;
import com.sipc.userserver.util.RedisUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final AcaMajorMapper acaMajorMapper;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    private final RedisUtil redisUtil;

    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper, AcaMajorMapper acaMajorMapper, MinioClient minioClient, MinioConfig minioConfig, RedisUtil redisUtil) {
        this.userInfoMapper = userInfoMapper;
        this.acaMajorMapper = acaMajorMapper;
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     * @author DoudiNCer
     */
    @Override
    public CommonResult<GetUserInfoResult> getUserInfo(Integer uid) {
        var userInfo = new UserInfo();
        var userIdKey = redisUtil.getUserIdKey(uid);
        var redisUi = redisUtil.get(userIdKey);
        if (redisUi instanceof UserInfo) {
                userInfo = (UserInfo) redisUi;
        } else {
            userInfo = userInfoMapper.selectById(uid);
            if (userInfo == null)
                return CommonResult.fail("查无此人");
            redisUtil.set(userIdKey, userInfo);
        }
        GetUserInfoResult result = new GetUserInfoResult();
        result.setUserId(uid);
        result.setUsername(userInfo.getUserName());
        if (userInfo.getAcaMajorId() != null) {
            AcaMajor acaMajor = acaMajorMapper.selectById(userInfo.getAcaMajorId());
            if (acaMajor == null)
                return CommonResult.fail("数据错误，查无专业信息");
            result.setAcademy(acaMajor.getAcaName());
            result.setMajor(acaMajor.getMajorName());
        }
        if (userInfo.getPhone() != null)
            result.setPhone(userInfo.getPhone());
        if (userInfo.getGender() != null)
            result.setGender(userInfo.getGender() ? "男" : "女");
        if (userInfo.getAvatarUrl() != null)
            result.setAvatarUrl(MinioUtil.getPictureURL(userInfo.getAvatarUrl()));
        return CommonResult.success(result);
    }

    /**
     * 创建新用户信息
     * @param param 新用户的 userID 与 openID
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> postNewUserInfo(PostNewUserIdParam param) {
        UserInfo ifexist = userInfoMapper.selectById(param.getUserId());
        if (ifexist != null)
            return CommonResult.fail("用户ID已存在");

        UserInfo ui = new UserInfo();
        ui.setUserId(param.getUserId());
        ui.setAvatarUrl("Head");
        ui.setCreateTime(LocalDateTime.now());
        ui.setUpdateTime(LocalDateTime.now());
        ui.setIsDeleted((byte) 0);
        int insert = userInfoMapper.insert(ui);
        if (insert != 1)
            return CommonResult.fail("服务器错误");
        return CommonResult.success("请求正常");
    }

    /**
     * 删除用户信息
     *
     * @param param 用户ID
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> dropUserInfo(DropUserInfoParam param) {
        int i = userInfoMapper.deleteById(param.getUserId());
        if (i != 1)
            return CommonResult.fail("系统错误");
        redisUtil.remove(redisUtil.getUserIdKey(param.getUserId()));
        return CommonResult.success("请求正常");
    }

    /**
     * 更新用户信息
     *
     * @param param 必须有用户ID（UserID），可选参数有用户名、性别、学院专业ID、手机号、用户头像
     * @return 处理结果
     * @author DoudiNCer
     */
    @Override
    public CommonResult<String> UpdateUserInfo(UpdateUserInfoParam param) {
        UserInfo userInfo = userInfoMapper.selectById(param.getUserId());
        if (userInfo == null)
            return CommonResult.fail("用户不存在");
        userInfo.setUserId(param.getUserId());
        userInfo.setUpdateTime(LocalDateTime.now());
        if (param.getUserName() != null && param.getUserName().length() != 0)
            userInfo.setUserName(param.getUserName());
        if (param.getAcaMajorId() != null && param.getAcaMajorId() != 0)
            userInfo.setAcaMajorId(param.getAcaMajorId());
        if (param.getPhone() != null && param.getPhone().length() != 0)
            userInfo.setPhone(param.getPhone());
        if (param.getGender() != null)
            userInfo.setGender(param.getGender());
        var update = userInfoMapper.updateById(userInfo);
        if (update == 0)
            return CommonResult.fail("没有数据被更新");
        else if (update > 1)
            return CommonResult.fail("数据库错误");
        else {
            String userIdKey = redisUtil.getUserIdKey(param.getUserId());
            redisUtil.remove(userIdKey);
            redisUtil.set(userIdKey, userInfo);
            return CommonResult.success("请求正常");
        }
    }

    /**
     * @param file 头像文件
     * @param param 用户ID
     * @return 处理结果
     */
    @Override
    public CommonResult<String> UpdateUserAvatar(MultipartFile file, UpdateUserAvatarParam param) {
        UserInfo userInfo = userInfoMapper.selectById(param.getUserId());
        if (userInfo == null)
            return CommonResult.fail("用户不存在");
        if (userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() != 0){
            try {
                minioClient.removeObject(RemoveObjectArgs.builder().
                        bucket(minioConfig.getBucketName())
                        .object(userInfo.getAvatarUrl() + MinioUtil.URLEnd)
                        .build());
            } catch (Throwable e) {
               log.warn("Minio Delete File Error: FileName: " + userInfo.getAvatarUrl() + "\n + Error: " + e.getMessage() );
            }
        }
        String newUserAvatar = MinioUtil.hashFile(userInfo.getUserId());
        InputStream is = null;
        try {
            is = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(newUserAvatar + MinioUtil.URLEnd)
                            .stream(is, file.getSize(), -1)
                            .build());
            is.close();
        } catch (Throwable e) {
            log.warn("Upload New Avatar Error: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (Throwable e){

            }
        }
        userInfo.setAvatarUrl(newUserAvatar);
        userInfoMapper.updateById(userInfo);
        String userIdKey = redisUtil.getUserIdKey(userInfo.getUserId());
        redisUtil.remove(userIdKey);
        redisUtil.set(userIdKey, userInfo);
        return CommonResult.success("请求正常");
    }
}
