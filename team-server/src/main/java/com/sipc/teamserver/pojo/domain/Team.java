package com.sipc.teamserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @author DoudiNCer
 * @since 2023-04-09
 */
@Getter
@Setter
@TableName("team")
public class Team implements Serializable {

    @Serial
    private static final long serialVersionUID = 111501L;

    /**
     * 队伍ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 帖子ID
     */
    @TableField("post_id")
    private Integer postId;

    /**
     * 开始组队时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 组队截止时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
