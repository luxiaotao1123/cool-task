package com.cool.task.subject;

import com.cool.task.common.enums.TaskMethodEnum;
import com.cool.task.common.pojo.Task;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Vincent
 */
public abstract class CoolSubject {

    public static LinkedBlockingQueue<Task> addTaskQueue;
    public static LinkedBlockingQueue<Task> modifyTaskQueue;
    public static LinkedBlockingQueue<Task> removeTaskQueue;

    static {
        addTaskQueue = new LinkedBlockingQueue<>();
        modifyTaskQueue = new LinkedBlockingQueue<>();
        removeTaskQueue = new LinkedBlockingQueue<>();
    }

    public void offer(Task task, TaskMethodEnum taskMethodEnum){};
}
