package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMsg {
    /**
     * 发送消息时间 yyyy-MM-dd HH:mm
     */
    private String time;
    /**
     * 发送消息的文本内容
     */
    private String message;
    /**
     * 微信用户唯一标识
     */
    private String openid;

    private String username;

    private String avatarUrl;

    public SendMsg() {
        //此无参构造用于反序列化，不可删除
    }
}
