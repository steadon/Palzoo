package com.sipc.loginserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author sterben
 * @since 2023-04-02
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("permission")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限等级
     */
    @TableField("level")
    private Integer level;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 软删除
     */
    @TableField("is_deleted")
    private Byte isDeleted;
}
