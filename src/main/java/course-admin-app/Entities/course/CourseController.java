package Avocado.main_project.Entities.course;

import Avocado.main_project.Entities.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import Avocado.main_project.Login.CustomUserDetails;

@Controller
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Display all courses
    @GetMapping("/courses")
    public String getCourses(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Retrieve all courses and add them to the model for rendering in the view
        model.addAttribute("courses", courseService.getAllCourses());
        // Add a new Course object to the model for handling course creation in the view
        model.addAttribute("newCourse", new Course());

        // Pass userDetails attributes to the model
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("name", userDetails.getName());
        model.addAttribute("role", userDetails.getRole());
        model.addAttribute("email", userDetails.getEmail());

        // Return the view name "courses" to render the list of courses
        return "courses";
    }

    // Add a new course
    @PostMapping("/courses")
    public String addCourse(Course newCourse) {
        // Create a new course with the provided name and redirect to the courses page
        courseService.createCourse(newCourse.getName());
        return "redirect:/courses";
    }

    // Delete a course
    @PostMapping("/courses/delete/{courseId}")
    public String deleteCourse(@PathVariable Long courseId) {
        // Retrieve the course by its ID, delete it, and redirect to the courses page
        Course course = courseService.getCourseById(courseId);
        courseService.deleteCourse(course);
        return "redirect:/courses";
    }

    // Display the view for importing students
    @GetMapping("/importStudents")
    public String getImportStudentsView() {
        // Return the view name "importStudents" to render the import students page
        return "importStudents";
    }

    // Display the view for importing teams
    @GetMapping("/importTeams")
    public String getImportTeamsView() {
        // Return the view name "importTeams" to render the import teams page
        return "importTeams";
    }
}
