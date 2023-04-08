package com.sipc.loginserver.pojo.domain;

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
 * @since 2023-04-02
 */
@Getter
@Setter
@TableName("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电话号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 微信uid
     */
    @TableField("openid")
    private String openid;

    /**
     * 权限等级
     */
    @TableField("permission_id")
    private Integer permissionId;

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
    @TableLogic
    @TableField("is_deleted")
    private Byte isDeleted;

    public User(String openid) {
        this.openid = openid;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
        this.permissionId = 2;
        this.isDeleted = 0;
        this.phone = null;
    }

    public User() {

    }
}
