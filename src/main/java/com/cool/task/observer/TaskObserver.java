package com.cool.task.observer;


import com.cool.task.common.enums.TaskMethodEnum;

public interface TaskObserver {

    void run(TaskMethodEnum taskMethodEnum);
}
