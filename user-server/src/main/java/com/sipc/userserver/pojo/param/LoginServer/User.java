package com.sipc.userserver.pojo.param.LoginServer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 微信uid
     */
    private String openid;

    /**
     * 权限等级
     */
    private Integer permissionId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 软删除
     */
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
