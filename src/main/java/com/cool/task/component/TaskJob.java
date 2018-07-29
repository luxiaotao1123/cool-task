package com.cool.task.component;

import com.cool.task.common.pojo.Task;
import com.cool.task.dao.TaskDao;
import com.cool.task.tcp.handler.OnlineServerHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Vincent
 */
@Component
public class TaskJob implements Job {

    private static Logger log = LoggerFactory.getLogger(TaskJob.class);


    private final TaskDao taskDao;

    @Autowired
    public TaskJob(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Collection<Object> values = jobExecutionContext.getMergedJobDataMap().values();
        for (Object obj : values){
            Task task = (Task) obj;
            OnlineServerHandler.sendMsgToClient(task.getName(), task.getParams());
            taskDao.deleteTask(task);
            log.info(" send msg to client id = {} success", task.getName());
        }
    }
}
