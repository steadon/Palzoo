package com.sipc.messageserver.mapper;

import com.sipc.messageserver.pojo.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tzih
 * @since 2023-04-10
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
