package com.cool.task.common.exception;

import java.io.Serializable;

public class TaskException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6214969058376774763L;

    public TaskException(){};

    public TaskException(String msg){
        super(msg);
    }

    public TaskException(String msg, Throwable throwable){
        super(msg, throwable);
    }

    public TaskException(Throwable throwable){
        super(throwable);
    }

}
