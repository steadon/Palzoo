package com.sipc.chatserver.mapper;

import com.sipc.chatserver.pojo.domain.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sipc.chatserver.service.cache.MybatisPlusRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sterben
 * @since 2023-04-04
 */
@Mapper
@CacheNamespace(implementation = MybatisPlusRedisCache.class)
public interface RoomMapper extends BaseMapper<Room> {

}
