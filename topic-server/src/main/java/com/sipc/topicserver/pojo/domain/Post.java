package com.sipc.topicserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author tzih
 * @since 2023-04-04
 */
@Getter
@Setter
@TableName("post")
public class Post implements Serializable {

    @Serial
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

    @TableField("is_finish")
    private Byte isFinish;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField("watch_num")
    private Integer watchNum;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("is_deleted")
    private Byte isDeleted;
}
