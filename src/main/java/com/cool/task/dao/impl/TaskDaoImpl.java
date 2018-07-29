package com.cool.task.dao.impl;

import com.cool.task.common.pojo.Task;
import com.cool.task.dao.TaskDao;
import com.cool.task.mapper.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Vincent
 */
@Component("taskDao")
public class TaskDaoImpl implements TaskDao{

    private static Logger log = LoggerFactory.getLogger(TaskDaoImpl.class);

    private final TaskMapper taskMapper;

    @Autowired
    public TaskDaoImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public List<Task> selectAll() {
        return taskMapper.selectAll();
    }

    @Override
    public Task selectTask(Task task) {
        return taskMapper.selectTask(task.getName());
    }

    @Override
    public void insertTask(Task task) {
        Date current = new Date();
        task.setCreateTime(current);
        task.setUpdateTime(current);
        if (taskMapper.insertTask(task) != 1){
            log.error("persist task name={} error", task.getName());
        }
    }

    @Override
    public void updateTask(Task task) {
        task.setUpdateTime(new Date());
        if (taskMapper.updateTask(task) != 1){
            log.error("persist task name={} error", task.getName());
        }
    }

    @Override
    public void deleteTask(Task task) {
        task.setUpdateTime(new Date());
        task.setDeleted(true);
        if (taskMapper.deleteTask(task) != 1){
            log.error("persist task name={} error", task.getName());
        }
    }
}
