package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenidAndPostId {
    /**
     * 微信用户唯一标识
     */
    private String openid;
    /**
     * 帖子ID
     */
    private String postId;

    public OpenidAndPostId() {
        //此无参构造用于反序列化，不可删除
    }
}
