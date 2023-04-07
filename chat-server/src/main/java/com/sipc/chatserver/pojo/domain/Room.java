package com.sipc.chatserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author sterben
 * @since 2023-04-04
 */
@Getter
@Setter
@TableName("room")
public class Room implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("post_id")
    private Integer postId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Byte isDeleted;

    public Room(String postId) {
        this.postId = Integer.valueOf(postId);
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    public Room() {

    }
}
