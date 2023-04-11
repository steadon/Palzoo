package com.sipc.topicserver.pojo.dto.param.messageServer;

import lombok.Data;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 10:40
 */
@Data
public class SendParam {

    Integer userId;

    Integer toUserId;

    String content;

}
