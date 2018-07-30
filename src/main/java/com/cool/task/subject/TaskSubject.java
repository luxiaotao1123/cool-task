package com.cool.task.subject;

import com.cool.task.common.enums.TaskMethodEnum;
import com.cool.task.common.pojo.Task;
import com.cool.task.observer.TaskObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Vincent
 */
@Component("taskSubject")
public class TaskSubject extends CoolSubject {

    private static Logger log = LoggerFactory.getLogger(TaskSubject.class);

    private final TaskObserver taskObserver;

    @Autowired
    public TaskSubject(TaskObserver taskObserver) {
        this.taskObserver = taskObserver;
    }


    @Override
    public void offer(Task task, TaskMethodEnum taskMethodEnum){

        TaskMethodEnum finTaskMeEnum = null;

        try {

            switch (taskMethodEnum){

                case FIND:
                    finTaskMeEnum = TaskMethodEnum.FIND;
                    findTaskQueue.put(task);
                    break;
                case ADD:
                    finTaskMeEnum = TaskMethodEnum.ADD;
                    addTaskQueue.put(task);
                    break;
                case MODIFY:
                    finTaskMeEnum = TaskMethodEnum.MODIFY;
                    modifyTaskQueue.put(task);
                    break;
                case REMOVE:
                    finTaskMeEnum = TaskMethodEnum.REMOVE;
                    removeTaskQueue.put(task);
                    break;
                default:
                    break;
            }

            taskObserver.run(finTaskMeEnum);

        }catch (Exception e){
            log.error(e.getMessage());
        }


    }

}
