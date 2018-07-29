package com.cool.task.common.pojo;


/**
 * @author  Vincent
 */
public class TCPMsgRequest {

    public static final int HEART_TYPE = 1;
    public static final int MESSAGE_TYPE = 2;

    private int type;

    private Integer msgLen;

    private String id;

    private String method;

    private Task task;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(Integer msgLen) {
        this.msgLen = msgLen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
