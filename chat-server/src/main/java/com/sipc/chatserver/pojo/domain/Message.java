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
@TableName("message")
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("openid")
    private String openid;

    @TableField("room_id")
    private Integer roomId;

    @TableField("message")
    private String message;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Byte isDeleted;

    public Message(String openid, Integer roomId, String message) {
        this.openid = openid;
        this.roomId = roomId;
        this.message = message;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    public Message() {

    }
}
