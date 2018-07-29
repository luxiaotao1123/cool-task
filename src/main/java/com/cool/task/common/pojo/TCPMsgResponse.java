package com.cool.task.common.pojo;



import com.cool.task.common.enums.TCPStatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  Vincent
 */
public class TCPMsgResponse extends HashMap<String, Object> {

    private final static String CODE = "code";
    private final static String MSG = "msg";

    public TCPMsgResponse() {
        put(CODE, TCPStatusEnum.SUCCESS.getCode());
        put(MSG, TCPStatusEnum.SUCCESS.getValue());
    }

    public static TCPMsgResponse error() {
        return error(TCPStatusEnum.ERROR.getCode(), TCPStatusEnum.ERROR.getValue());
    }

    public static TCPMsgResponse error(String msg) {
        return error(TCPStatusEnum.ERROR.getCode(), msg);
    }

    public static TCPMsgResponse error(int code, String msg) {
        TCPMsgResponse r = new TCPMsgResponse();
        r.put(CODE, code);
        r.put(MSG, msg);
        return r;
    }

    public static TCPMsgResponse ok(String msg) {
        TCPMsgResponse r = new TCPMsgResponse();
        r.put(MSG, msg);
        return r;
    }

    public static TCPMsgResponse ok(Map<String, Object> map) {
        TCPMsgResponse r = new TCPMsgResponse();
        r.putAll(map);
        return r;
    }

    public static TCPMsgResponse ok() {
        return new TCPMsgResponse();
    }

    public TCPMsgResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
