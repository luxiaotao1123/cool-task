package com.cool.task.common.enums;

/**
 * @author  Vincent
 */
public enum HttpStatusEnum {

    SUCCESS(200, "请求成功"),
    ERROR(500, "服务器异常"),
    EMPTY_PARAMS(400,"参数为空"),
    ;

    private Integer code;
    private String value;

    HttpStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }


}
