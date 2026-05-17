package main;

import student.StudentManager;

import java.util.Scanner;

/**
 * SRP (Single Responsibility Principle):
 * StudentMenuHandler sirf student module ka console UI handle karta hai —
 * user input read karna, menu print karna, aur StudentManager ko delegate.
 * Business logic yahan nahi — sirf routing.
 *
 * DIP (Dependency Inversion Principle):
 * StudentManager inject hota hai — concrete implementation change karna ho
 * to yeh class chhoni nahi padegi.
 *
 * CODE SMELL REMOVED — Magic Numbers (menu choices):
 * Saare menu choices named constants mein — agar order change karna ho to
 * ek jagah change hoga, switch aur printMenu() dono automatically update.
 *
 * CODE SMELL REMOVED — Long Method (run()):
 * Har case mein minimal logic hai — asli kaam StudentManager ko delegate.
 */
public class StudentMenuHandler {

    // Menu option constants — ek jagah change karo, sab jagah lagoo
    private static final int STU_ADD     = 1;
    private static final int STU_VIEW    = 2;
    private static final int STU_UPDATE  = 3;
    private static final int STU_DELETE  = 4;
    private static final int STU_SEARCH  = 5;
    private static final int STU_ENROLL  = 6;
    private static final int STU_COURSES = 7;
    private static final int STU_DETAILS = 8;
    private static final int STU_REPORT  = 9;   // NEW — OCP: existing constants untouched
    private static final int STU_BACK    = 10;

    private final Scanner        sc;
    private final StudentManager studentManager;

    public StudentMenuHandler(Scanner sc, StudentManager studentManager) {
        this.sc             = sc;
        this.studentManager = studentManager;
    }

    /** Student module ka main loop — jab tak user "Back" na choose kare. */
    public void run() {
        boolean active = true;
        while (active) {
            printMenu();
            int ch = MenuUtils.readInt(sc);
            try {
                switch (ch) {
                    case STU_ADD: {
                        System.out.print("ID: ");   String id   = sc.nextLine();
                        System.out.print("Name: "); String name = sc.nextLine();
                        studentManager.add(id, name);
                        break;
                    }
                    case STU_VIEW:
                        studentManager.view();
                        break;
                    case STU_UPDATE: {
                        System.out.print("ID: ");       String id   = sc.nextLine();
                        System.out.print("New Name: "); String name = sc.nextLine();
                        studentManager.update(id, name);
                        break;
                    }
                    case STU_DELETE: {
                        System.out.print("ID: ");
                        studentManager.delete(sc.nextLine());
                        break;
                    }
                    case STU_SEARCH: {
                        System.out.print("ID: ");
                        studentManager.search(sc.nextLine());
                        break;
                    }
                    case STU_ENROLL: {
                        System.out.print("Student ID: "); String id     = sc.nextLine();
                        System.out.print("Course: ");     String course = sc.nextLine();
                        studentManager.enrollCourse(id, course);
                        break;
                    }
                    case STU_COURSES: {
                        System.out.print("ID: ");
                        studentManager.viewCourses(sc.nextLine());
                        break;
                    }
                    case STU_DETAILS: {
                        System.out.print("ID: ");
                        studentManager.studentDetails(sc.nextLine());
                        break;
                    }
                    case STU_REPORT: {
                        // NEW — OCP: studentManager.generateReport() delegates to StudentReportService
                        System.out.print("Student ID: ");
                        studentManager.generateReport(sc.nextLine());
                        break;
                    }
                    case STU_BACK:
                        active = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                // Business validation errors gracefully print hote hain
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** Student module ka menu print karta hai. */
    private void printMenu() {
        System.out.println("\n--- STUDENT MODULE ---");
        System.out.println(STU_ADD     + ". Add Student");
        System.out.println(STU_VIEW    + ". View All Students");
        System.out.println(STU_UPDATE  + ". Update Student");
        System.out.println(STU_DELETE  + ". Delete Student");
        System.out.println(STU_SEARCH  + ". Search Student");
        System.out.println(STU_ENROLL  + ". Enroll in Course");
        System.out.println(STU_COURSES + ". View Student Courses");
        System.out.println(STU_DETAILS + ". Student Details");
        System.out.println(STU_REPORT  + ". Student Academic Report");   // NEW
        System.out.println(STU_BACK    + ". Back");
        System.out.print("Action: ");
    }

}
