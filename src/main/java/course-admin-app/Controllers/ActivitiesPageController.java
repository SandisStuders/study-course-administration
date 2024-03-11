package Avocado.main_project.Controllers;

import Avocado.main_project.Entities.activity.Activity;
import Avocado.main_project.Entities.activity.ActivityService;
import Avocado.main_project.Entities.task.Task;
import Avocado.main_project.Entities.task.TaskService;
import Avocado.main_project.Login.CustomUserDetails;
import Avocado.main_project.Entities.user.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ActivitiesPageController {

    private final TaskService taskService;
    private final ActivityService activityService;

    // Constructor to initialize the controller with necessary services
    public ActivitiesPageController(TaskService taskService, ActivityService activityService, UserService userService) {
        this.taskService = taskService;
        this.activityService = activityService;
    }

    // Handler for displaying the activities page
    @GetMapping("/activities/{activityId}")
    public String activitiesPage(@PathVariable Long activityId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Fetch activity details by ID
        Activity activity = activityService.getActivityById(activityId);

        // Add attributes to the model for rendering in the view
        model.addAttribute("activityId", activity.getId());
        model.addAttribute("activityName", activity.getName());
        model.addAttribute("tasks", getTasks(activityId));

        // Add model attribute for the new task form
        model.addAttribute("newTask", new Task());

        // Pass userDetails attributes to the model
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("name", userDetails.getName());
        model.addAttribute("role", userDetails.getRole());
        model.addAttribute("email", userDetails.getEmail());
        model.addAttribute("courseId", activity.getCourseId());

        return "activitiesPage";
    }

    // Helper method to retrieve tasks by activity ID
    private List<Task> getTasks(Long id) {
        return taskService.getTasksByActivity(id);
    }

    // Handler for processing the form submission for adding a new task
    @PostMapping("/activities/{activityId}/addTask")
    public String addTask(@PathVariable Long activityId, @ModelAttribute Task newTask) {
        System.out.println("Received activityId: " + activityId);

        // Set the activity ID before saving the task
        newTask.setActivityId(activityId);

        // Save the new task using the task service
        taskService.createTask(newTask);

        // Redirect to the activities page for the specified activity
        return "redirect:/activities/" + activityId;
    }

    // Handler for processing the form submission for deleting a task
    @PostMapping("/activities/{activityId}/delete/{taskId}")
    public String deleteTask(@PathVariable Long activityId, @PathVariable Long taskId) {
        // Fetch the task by ID
        Task task = taskService.getTaskById(taskId);

        // Delete the task using the task service
        taskService.deleteTask(task);

        // Redirect to the activities page for the specified activity
        return "redirect:/activities/" + activityId;
    }
}
