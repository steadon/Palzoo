package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMsg {
    private String time;
    private String message;
    private String openid;

    public SendMsg() {

    }
}
