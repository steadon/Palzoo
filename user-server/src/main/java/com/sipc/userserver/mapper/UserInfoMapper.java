package com.sipc.userserver.mapper;

import com.sipc.userserver.pojo.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sterben
 * @since 2023-04-05
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
