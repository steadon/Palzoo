package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String message;

    public ChatMessage() {

    }
}

