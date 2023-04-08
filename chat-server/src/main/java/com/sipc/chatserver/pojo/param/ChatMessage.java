package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String message;

    public ChatMessage() {

    }
}

