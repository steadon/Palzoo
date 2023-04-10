package com.sipc.messageserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author tzih
 * @since 2023-04-10
 */
@Getter
@Setter
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("content")
    private String content;

    @TableField("user_id")
    private Integer userId;

    @TableField("to_user_id")
    private Integer toUserId;

    @TableField("time")
    private LocalDateTime time;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
