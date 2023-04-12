package com.sipc.chatserver.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author o3141
 * @version 1.0
 * @since 2023/4/4 21:12
 */
@Data
@AllArgsConstructor
public class DeleteParam {

    private Integer userId;

    private Integer postId;

    public DeleteParam() {

    }
}
