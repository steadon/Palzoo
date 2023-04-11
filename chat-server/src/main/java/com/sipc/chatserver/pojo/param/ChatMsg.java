package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMsg {
    /**
     * 聊天信息文本内容
     */
    private String message;

    public ChatMsg() {
        //此无参构造用于反序列化，不可删除
    }
}

