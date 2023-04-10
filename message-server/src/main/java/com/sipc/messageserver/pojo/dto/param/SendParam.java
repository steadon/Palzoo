package com.sipc.messageserver.pojo.dto.param;

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
