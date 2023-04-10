package com.sipc.messageserver.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipc.messageserver.pojo.dto.resultEnum.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> {

    /**
     * 响应参数
     */
    private String code;
    private String message;
    private T data;


    /**
     * 返回成功消息通知
     *
     * @param message 成功信息
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 返回成功结果
     *
     * @param data 成功结果
     * @param <T>  返回对象封装类
     * @return 返回请求对象
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 返回失败消息通知
     *
     * @param message 错误信息
     */
    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<>(ResultEnum.FAILED.getCode(), message, null);
    }
}
