package Avocado.main_project.Controllers;

import Avocado.main_project.Entities.activity.Activity;
import Avocado.main_project.Entities.activity.ActivityRepository;
import Avocado.main_project.Entities.activity.ActivityService;
import Avocado.main_project.Entities.criterion.Criterion;
import Avocado.main_project.Entities.criterion.CriterionRepository;
import Avocado.main_project.Entities.criterion.CriterionService;
import Avocado.main_project.Login.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Map;

@Controller
public class CriterionPageController {

    private final CriterionService criterionService;
    private final ActivityService activityService;

    public CriterionPageController(CriterionService criterionService, ActivityService activityService) {
        this.criterionService = criterionService;
        this.activityService = activityService;
    }

    // Handler for displaying all criteria for a given course
    @GetMapping("/criteria/{courseId}")
    public String viewAllCriteria(@PathVariable Long courseId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails ) {
        // Retrieve criteria and activities for the specified course
        List<Criterion> allCriteria = criterionService.getCriteriaByCourseId(courseId);
        List<Activity> allActivities = activityService.getActivitiesByCourse(courseId);

        // Add attributes to the model for rendering in the view
        model.addAttribute("criteria", allCriteria);
        model.addAttribute("newCriterion", new Criterion());
        model.addAttribute("activities", allActivities);
        model.addAttribute("courseId", courseId);

        // Pass userDetails attributes to the model
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("name", userDetails.getName());
        model.addAttribute("role", userDetails.getRole());
        model.addAttribute("email", userDetails.getEmail());

        return "criterionPage";
    }

    // Handler for processing the form submission for adding a new criterion
    @PostMapping("/criteria/{courseId}/add")
    public String addCriterion(@ModelAttribute Criterion newCriterion, @PathVariable Long courseId) {
        // Set the course ID before saving the new criterion
        newCriterion.setCourseId(courseId);
        criterionService.createCriterion(newCriterion);
        return "redirect:/criteria/" + courseId;
    }

    // Handler for processing the form submission for deleting a criterion
    @PostMapping("/criteria/{courseId}/delete")
    public String deleteCriterion(@RequestParam Long criterionId, @PathVariable Long courseId) {
        // Fetch the criterion by ID and delete it
        Criterion criterion = criterionService.getCriterionById(criterionId);
        criterionService.deleteCriterion(criterion);
        return "redirect:/criteria/" + courseId;
    }

    // Handler for displaying the edit form for a specific criterion
    @GetMapping("/criteria/{courseId}/edit/{criterionId}")
    public String showEditForm(@PathVariable Long courseId, @PathVariable Long criterionId, Model model) {
        // Fetch the existing criterion and other required data for the edit form
        Criterion existingCriterion = criterionService.getCriterionById(criterionId);
        List<Activity> allActivities = activityService.getActivitiesByCourse(courseId);
        List<Criterion> allCriteria = criterionService.getCriteriaByCourseId(courseId);

        // Add attributes to the model for rendering in the view
        model.addAttribute("editedCriterion", existingCriterion);
        model.addAttribute("editMode", true);
        model.addAttribute("newCriterion", new Criterion());
        model.addAttribute("criteria", allCriteria);
        model.addAttribute("activities", allActivities);
        model.addAttribute("courseId", courseId);

        return "criterionPage"; // Redirect to the page that will handle displaying the edited form
    }

    // Handler for processing the form submission for editing a criterion
    @PostMapping("/criteria/{courseId}/edit/{criterionId}")
    public String editCriterion(@ModelAttribute Criterion editedCriterion, @PathVariable Long courseId, @PathVariable Long criterionId) {
        // Set the ID for the edited criterion and update it
        editedCriterion.setId(criterionId);
        criterionService.updateCriterion(editedCriterion.getId(), editedCriterion.getCourseId(),
                editedCriterion.getCategory(), editedCriterion.getCriterionName(),
                editedCriterion.getCheckProcedure(), editedCriterion.getEvaluationMethod(),
                editedCriterion.getMaxPoints());
        return "redirect:/criteria/" + courseId;
    }
}
