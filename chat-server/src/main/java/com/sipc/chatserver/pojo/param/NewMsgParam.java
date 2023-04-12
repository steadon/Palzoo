package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewMsgParam {
    private Integer postId;
    private String username;
    private String msg;

    public NewMsgParam() {

    }
}
