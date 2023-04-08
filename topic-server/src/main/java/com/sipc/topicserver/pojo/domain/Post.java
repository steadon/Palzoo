package com.sipc.topicserver.pojo.domain;

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
 * @since 2023-04-06
 */
@Getter
@Setter
@TableName("post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("title")
    private String title;

    /**
     * 简介
     */
    @TableField("brief")
    private String brief;

    @TableField("content")
    private String content;

    @TableField("author_id")
    private Integer authorId;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("category_next_id")
    private Integer categoryNextId;

    /**
     * 性别限制 0 不限 1男性 2女性
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 人数限制
     */
    @TableField("number")
    private Integer number;

    @TableField("is_finish")
    private Byte isFinish;

    /**
     * 出发时间
     */
    @TableField("go_time")
    private LocalDateTime goTime;

    /**
     * 组队开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 组队结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("watch_num")
    private Integer watchNum;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;
}
