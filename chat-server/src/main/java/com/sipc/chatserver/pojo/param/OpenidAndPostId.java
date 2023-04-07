package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenidAndPostId {
    private String openid;
    private String postId;

    public OpenidAndPostId() {
    }
}
