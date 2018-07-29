package com.cool.task.mapper;

import com.cool.task.common.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Vincent
 */
@Mapper
@Repository
public interface TaskMapper {

    List<Task> selectAll();

    Task selectTask(String name);

    int insertTask(Task task);

    int updateTask(Task task);

    int deleteTask(Task task);
}
