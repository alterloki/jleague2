package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.repository.TaskDao;
import ru.jleague13.timer.TaskManagementSystem;

/**
 * @author ashevenkov 26.09.16 14:50.
 */
@Controller
@RequestMapping("/new/admin/tasks")
public class TaskController {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskManagementSystem taskManagementSystem;

    @RequestMapping(method = RequestMethod.GET)
    public String showAllTasks(Model model) {
        model.addAttribute("taskList", taskDao.getTaskInfoList());
        return "admin/tasks";
    }

    @RequestMapping(value="/{task_name}", method = RequestMethod.GET)
    public String showTaskLogs(@PathVariable("task_name") String taskName, Model model) {
        model.addAttribute("taskList", taskDao.getTaskLastLogs(taskName, 10));
        return "admin/task-log";
    }

    @RequestMapping(value="/{task_name}/run", method = RequestMethod.GET)
    public String runTask(@PathVariable("task_name") String taskName) {
        taskManagementSystem.runTask(taskName);
        return "redirect:/new/admin/tasks";
    }
}
