package com.sipc.messageserver.pojo.dto.result.chat;

import lombok.Data;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/12 20:13
 */
@Data
public class NewMsgParam {

    private Integer postId;

    private String username;

    private String msg;

}
