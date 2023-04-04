package com.sipc.chatserver.mapper;

import com.sipc.chatserver.pojo.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sterben
 * @since 2023-04-04
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
