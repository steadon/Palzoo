package com.sipc.topicserver.pojo.resultEnum;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS("00000", "请求正常"), FAILED("A0400", "请求失败");

    private final String code;
    private final String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
