package com.cool.task.dao;

import com.cool.task.common.pojo.Task;

import java.util.List;

public interface TaskDao {

    List<Task> selectAll();

    Task selectTask(Task task);

    void insertTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);
}
