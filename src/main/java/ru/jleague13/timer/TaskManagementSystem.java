package ru.jleague13.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import ru.jleague13.repository.TaskDao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ashevenkov 24.09.16 17:59.
 */
@Component
public class TaskManagementSystem implements InitializingBean, ApplicationContextAware, DisposableBean {

    private Log log = LogFactory.getLog(TaskManagementSystem.class);

    private Map<String, FaTask> tasks = new HashMap<>();
    private ThreadPoolTaskScheduler taskScheduler;
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private TaskDao taskDao;
    private ApplicationContext applicationContext;

    public TaskManagementSystem() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.afterPropertiesSet();
        taskScheduler = threadPoolTaskScheduler;
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void addTask(FaTask faTask) {
        log.info("Adding task " + faTask.getName() + " cron = " + faTask.getCron());
        tasks.put(faTask.getName(), faTask);
        taskScheduler.schedule(faTask.getToRun(), new CronTrigger(faTask.getCron()));
        taskDao.registerTask(faTask.getName(), faTask.getCron());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing TMS");
        taskDao.deleteAllTasks();
        Map<String, FaTask> appTasks = applicationContext.getBeansOfType(FaTask.class);
        for (String taskName : appTasks.keySet()) {
            FaTask faTask = appTasks.get(taskName);
            addTask(faTask);
        }
    }

    public void logStatus(String taskName, TaskStatus status, String message) {
        taskDao.addStatus(taskName, status, message, new Date());
    }

    public void sendProgress(String taskName, int progress) {
        log.info(taskName + " progress is " + progress);
        taskDao.updateProgress(taskName, progress);
    }

    @Override
    public void destroy() throws Exception {
        taskScheduler.destroy();
        taskExecutor.destroy();
    }

    public void runTask(String taskName) {
        FaTask faTask = tasks.get(taskName);
        if (faTask != null) {
            taskExecutor.execute(faTask::doOneRun);
        }
    }
}
