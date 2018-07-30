package com.cool.task.observer.impl;

import com.alibaba.fastjson.JSON;
import com.cool.task.common.enums.TaskMethodEnum;
import com.cool.task.component.TaskBus;
import com.cool.task.dao.TaskDao;
import com.cool.task.observer.TaskObserver;
import com.cool.task.subject.CoolSubject;
import com.cool.task.tcp.handler.OnlineServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author Vincent
 */
@EnableAsync
@Component("taskObserver")
public class TaskObserverImpl implements TaskObserver {

    private static Logger log = LoggerFactory.getLogger(TaskObserverImpl.class);


    @Autowired
    public TaskObserverImpl(TaskBus taskBus, TaskDao taskDao) {
        this.taskBus = taskBus;
        this.taskDao = taskDao;
    }

    private final TaskBus taskBus;
    private final TaskDao taskDao;

    @Async
    @Override
    public void run(TaskMethodEnum taskMethodEnum) {

        switch (taskMethodEnum){
            case FIND:
                find();
                break;
            case ADD:
                add();
                break;
            case MODIFY:
                modify();
                break;
            case REMOVE:
                remove();
                break;
            default:
                break;

        }

    }

    private void find(){
        Optional.ofNullable(CoolSubject.findTaskQueue.poll()).
                ifPresent(task -> {
                    taskDao.selectTask(task);
                    OnlineServerHandler.sendMsgToClient(task.getName(), JSON.toJSONString(task));
                });
    }

    private void add(){
        Optional.ofNullable(CoolSubject.addTaskQueue.poll()).
                ifPresent(task -> {
                    taskBus.addTask(task);
                    taskDao.insertTask(task);
                });
    }

    private void modify(){
        Optional.ofNullable(CoolSubject.modifyTaskQueue.poll()).
                ifPresent(task -> {
                    taskBus.modifyTask(task);
                    taskDao.updateTask(task);
                });
    }

    private void remove(){
        Optional.ofNullable(CoolSubject.removeTaskQueue.poll()).
                ifPresent(task -> {
                    taskBus.removeTask(task);
                    taskDao.deleteTask(task);
                });
    }
}
