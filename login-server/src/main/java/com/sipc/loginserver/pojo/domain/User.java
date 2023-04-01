package com.sipc.loginserver.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author sterben
 * @since 2023-04-02
 */
@Getter
@Setter
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

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
    @TableField("is_deleted")
    private Byte isDeleted;
}
