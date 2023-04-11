package com.sipc.topicserver.pojo.dto.param;

import lombok.Data;

/**
 * 完成帖子接口所需的参数
 * @author o3141
 * @since 2023/4/3 23:17
 * @version 1.0
 */
@Data
public class FinishParam {

    /**
     * 用户id，帖子的创建者或管理员<p>
     * （目前管理员的相关处理有安全风险）
     */
    private Integer userId;

    /**
     * 帖子id
     */
    private Integer postId;

}
