package com.sipc.topicserver.mapper;

import com.sipc.topicserver.pojo.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tzih
 * @since 2023-04-05
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
