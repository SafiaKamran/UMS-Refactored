package student;

import common.IGradeStrategy;
import java.util.Scanner;

/**
 * OCP (Open/Closed Principle):
 * Student module mein naya "Report" feature add kiya gaya hai bina
 * StudentManager, Student, ya StudentDisplayService modify kiye.
 * Existing code closed for modification, open for extension.
 *
 * SRP (Single Responsibility Principle):
 * StudentReportService ki sirf ek zimmedari — ek student ka comprehensive
 * academic report generate aur print karna.
 *
 * DIP (Dependency Inversion Principle):
 * IStudent aur IGradeStrategy — dono abstractions par depend karta hai.
 */
public class StudentReportService {

    private static final int PASS_THRESHOLD = 50;
    private final IGradeStrategy gradeStrategy;

    /**
     * @param gradeStrategy marks se letter grade calculate karne ki strategy
     */
    public StudentReportService(IGradeStrategy gradeStrategy) {
        this.gradeStrategy = gradeStrategy;
    }

    /**
     * Student ka full academic report console par print karta hai.
     * Agar marks entered nahi hain (0 hain), toh pehle user se input leta hai.
     *
     * @param student report jis student ke liye generate hogi
     */
    public void print(IStudent student) {
        // Agar marks 0 hain, toh user se real-time input lein
        if (student.getMarks() == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter marks for student " + student.getName() + " (0-100): ");
            try {
                int inputMarks = Integer.parseInt(scanner.nextLine());
                if (inputMarks >= 0 && inputMarks <= 100) {
                    student.setMarks(inputMarks); // Student object mein marks save ho jayenge
                } else {
                    System.out.println("Invalid marks range! Showing default report.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Showing default report.");
            }
        }

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       STUDENT ACADEMIC REPORT    ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("ID           : " + student.getId());
        System.out.println("Name         : " + student.getName());
        // EMAIL LINE HAS BEEN REMOVED FROM HERE FOR CLEANER DISPLAY
        System.out.println("Marks        : " + student.getMarks() + " / 100");
        
        // Grade calculation strategy ko delegate
        System.out.println("Grade        : " + gradeStrategy.calculate(student.getMarks()));
        System.out.println("Status       : " + resolveStatus(student.getMarks()));
        System.out.println("Courses      : " + student.getCourses().size() + " enrolled");
        
        if (student.getCourses().isEmpty()) {
            System.out.println("             (No courses enrolled yet)");
        } else {
            student.getCourses().forEach(c -> System.out.println("             - " + c));
        }
        System.out.println("══════════════════════════════════════");
    }

    // ---- private helper ----

    private String resolveStatus(int marks) {
        return marks >= PASS_THRESHOLD ? "PASS ✓" : "FAIL ✗";
    }
}