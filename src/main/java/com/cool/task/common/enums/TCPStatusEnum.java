package com.cool.task.common.enums;



/**
 * @author  Vincent
 */
public enum TCPStatusEnum {

    SUCCESS(200, "请求成功"),
    ERROR(500, "服务器异常"),

    ;

    private Integer code;
    private String value;

    TCPStatusEnum(Integer code, String value) {
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
