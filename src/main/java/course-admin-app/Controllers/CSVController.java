package Avocado.main_project.Controllers;

import Avocado.main_project.Services.CSVService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    private CSVService csvService;

    // Endpoint for importing students from a CSV file
    @PostMapping("/import/students")
    public String importStudents(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file and trigger the import process
            String filePath = saveUploadedFile(file);
            csvService.importStudents(filePath);
            return "redirect:/courses"; // Redirect to courses page after successful import
        } catch (Exception e) {
            return "Error importing students: " + e.getMessage(); // Return an error message in case of failure
        }
    }

    // Endpoint for importing teams from a CSV file
    @PostMapping("/import/teams")
    public String importTeams(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file and trigger the import process
            String filePath = saveUploadedFile(file);
            csvService.importTeams(filePath);
            return "redirect:/courses"; // Redirect to courses page after successful import
        } catch (Exception e) {
            return "Error importing teams: " + e.getMessage(); // Return an error message in case of failure
        }
    }

    // Helper method to save an uploaded file to a temporary directory
    private String saveUploadedFile(MultipartFile file) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + file.getOriginalFilename();
        File tempFile = new File(filePath);
        file.transferTo(tempFile);
        return filePath;
    }

    // Endpoint for exporting evaluations to a CSV file
    @GetMapping("/export/evaluations/{courseId}")
    public void exportEvaluations(HttpServletResponse response, @PathVariable Long courseId) throws IOException {
        // Set the response headers for CSV file download
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=evaluations.csv");

        // Get the writer and trigger the export process
        Writer writer = response.getWriter();
        csvService.exportEvaluations(writer, courseId);
    }
}
