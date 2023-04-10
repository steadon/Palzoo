package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMsg {
    private String message;

    public ChatMsg() {

    }
}

