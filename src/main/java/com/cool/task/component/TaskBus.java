package com.cool.task.component;


import com.cool.task.common.pojo.Task;
import com.cool.task.dao.TaskDao;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * @author Vincent
 */
@Component("taskBus")
public class TaskBus {

    private static Logger log = LoggerFactory.getLogger(TaskBus.class);

    private String JOB_DETAIL_GROUP = "job_detail_group";
    private String JOB_TRIGGER_GROUP = "job_trigger_group";

    @Autowired
    public TaskBus(TaskDao taskDao, TaskJob taskJob) {
        this.taskDao = taskDao;
        this.taskJob = taskJob;
    }

    private final TaskDao taskDao;
    private final TaskJob taskJob;
    private Scheduler scheduler;
    private JobDetail jobDetail;
    private JobKey jobKey;

    /*
     * when the class is new
     */
    {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.setJobFactory(new JobFactory() {
                @Override
                public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {

                    if (bundle.getJobDetail().getJobClass().equals(TaskJob.class)) {
                        return taskJob;
                    }
                    try {
                        return bundle.getJobDetail().getJobClass().newInstance();
                    } catch (Exception ignore) {
                    }
                    return null;
                }
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * init scheduler
     */
    @PostConstruct
    public void initScheduler(){
        log.info("start init task process ......");
        try {
            List<Task> allTasks = taskDao.selectAll();
            if (allTasks == null || allTasks.isEmpty()){
                log.info("there is no task to run ......");
                return;
            }
            allTasks.forEach(task -> {
                try {
                    initTask(task.getName());
                    jobDetail.getJobDataMap().put(task.getName(), task);
                    scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().
                            withIdentity(task.getName(), JOB_TRIGGER_GROUP).
                            withSchedule(CronScheduleBuilder.cronSchedule(task.getCron())).
                            build());
                } catch (SchedulerException ignore) {
                }
            });
            scheduler.start();
        }catch (Exception e){
            log.error(" init task process defeat !!!");
            throw new RuntimeException(e);
        }
    }

    /**
     * init task
     */
    private boolean initTask(String name){
        if (name == null || "".equals(name)){
            return false;
        }
        try {
            jobKey = new JobKey(name, JOB_DETAIL_GROUP);
            jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null){
                jobDetail = newJob(TaskJob.class).withIdentity(name, JOB_DETAIL_GROUP).build();
            }
            return true;
        } catch (SchedulerException ignore) {
            return false;
        }
    }

    /**
     * add task
     */
    public boolean addTask(Task task){
        try {
            if (scheduler.checkExists(new JobKey(task.getName(), JOB_DETAIL_GROUP))){
                log.warn("the task name ={} exist", task.getName());
                return false;
            }
            if (!initTask(task.getName())){
                return false;
            }
            jobDetail.getJobDataMap().put(task.getName(), task);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getName(), JOB_TRIGGER_GROUP).withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isStarted()){
                scheduler.start();
            }
            log.info("add task name={} success", task.getName());
            return true;
        } catch (Exception e){
            log.error("add task name={} error", task.getName());
        }
        return false;
    }

    /**
     * modify task
     */
    public boolean modifyTask(Task task){
        try {
            TriggerKey triggerKey = new TriggerKey(task.getName(), JOB_TRIGGER_GROUP);
            if (!scheduler.checkExists(triggerKey)){
                log.warn("the task name ={} not exist", task.getName());
                return false;
            }
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getName(), JOB_TRIGGER_GROUP).withSchedule(cronScheduleBuilder).build();
            trigger.getJobDataMap().put(task.getName(), task);
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("modify task name={} success", task.getName());
            return true;
        } catch (Exception ignore){
            log.error("modify task name={} error", task.getName());
        }
        return false;
    }

    /**
     * remove task
     */
    public boolean removeTask(Task task){
        try {
            jobKey = new JobKey(task.getName(), JOB_DETAIL_GROUP);
            if (!scheduler.checkExists(jobKey)){
                log.warn("the task name ={} not exist", task.getName());
                return true;
            }
            scheduler.deleteJob(jobKey);
            log.info("remove task name={} success", task.getName());
            return true;
        } catch (Exception ignore){
            log.error("remove task name={} error", task.getName());
        }
        return false;
    }

    @PreDestroy
    public void destroyScheduler(){
        try {
            if (scheduler != null && !scheduler.isShutdown()){
                scheduler.shutdown();
                log.info("destroy scheduler success");
            }
        } catch (SchedulerException e) {
            log.warn("destroy scheduler error", e);
        }
    }
}
