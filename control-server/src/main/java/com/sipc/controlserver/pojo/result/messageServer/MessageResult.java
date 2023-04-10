package com.sipc.controlserver.pojo.result.messageServer;

import lombok.Data;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 15:59
 */
@Data
public class MessageResult {

    private String authorName;

    private String content;

    private String time;

}
