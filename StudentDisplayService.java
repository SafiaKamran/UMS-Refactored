package student;

import common.IGradeStrategy;
import common.IDisplayable;

/**
 * SRP (Single Responsibility Principle):
 * Sirf student information console par print karna — koi data mutation nahi,
 * koi business logic nahi.
 *
 * OCP (Open/Closed Principle):
 * IGradeStrategy inject hoti hai — grading logic change karne ke liye yeh
 * class modify nahi karni padegi.
 *
 * DIP (Dependency Inversion Principle):
 * IStudent aur IGradeStrategy dono abstractions par depend karta hai —
 * concrete Student ya StandardGradeStrategy par nahi.
 *
 * CODE SMELL REMOVED — Feature Envy:
 * Pehle kuch implementations mein display logic Manager class mein ghus jaati
 * thi. Ab display ki zimmedari clearly is dedicated service mein hai.
 */
public class StudentDisplayService implements IDisplayable {

    private final IStudent       student;
    private final IGradeStrategy gradeStrategy;

    public StudentDisplayService(IStudent student, IGradeStrategy gradeStrategy) {
        this.student       = student;
        this.gradeStrategy = gradeStrategy;
    }

    @Override
    public void display() {
        System.out.println("=== Student Details ===");
        System.out.println("ID      : " + student.getId());
        System.out.println("Name    : " + student.getName());
        System.out.println("Email   : " + student.getEmail());
        System.out.println("Courses : " + student.getCourses());
        System.out.println("Marks   : " + student.getMarks());
        // Grade calculation yahan nahi hoti — strategy ko delegate kiya gaya hai (SRP)
        System.out.println("Grade   : " + gradeStrategy.calculate(student.getMarks()));
    }
}
