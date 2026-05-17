package main;

import teacher.Teacher;
import teacher.TeacherManager;

import java.util.Scanner;

/**
 * SRP (Single Responsibility Principle):
 * Teacher module ka console UI handle karta hai sirf — user input routing.
 * Business logic TeacherManager mein hai.
 *
 * DIP (Dependency Inversion Principle):
 * TeacherManager inject hota hai — tight coupling nahi hai.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * Saare menu choices named constants mein.
 */
public class TeacherMenuHandler {

    private static final int TCH_ADD          = 1;
    private static final int TCH_VIEW         = 2;
    private static final int TCH_UPDATE       = 3;
    private static final int TCH_DELETE       = 4;
    private static final int TCH_SEARCH       = 5;
    private static final int TCH_LOAD         = 6;
    private static final int TCH_REPORT       = 7;
    private static final int TCH_SALARY_RPT   = 8;   // NEW — OCP: existing constants untouched
    private static final int TCH_BACK         = 9;

    private final Scanner        sc;
    private final TeacherManager teacherManager;

    public TeacherMenuHandler(Scanner sc, TeacherManager teacherManager) {
        this.sc             = sc;
        this.teacherManager = teacherManager;
    }

    /** Teacher module ka main loop — jab tak user "Back" na choose kare. */
    public void run() {
        boolean active = true;
        while (active) {
            printMenu();
            int ch = MenuUtils.readInt(sc);
            try {
                switch (ch) {
                    case TCH_ADD: {
                        System.out.print("ID: ");         String id   = sc.nextLine();
                        System.out.print("Name: ");       String name = sc.nextLine();
                        Teacher t = new Teacher(id, name);
                        System.out.print("Department: "); t.setDepartment(sc.nextLine());
                        System.out.print("Salary: ");     t.setSalary(Double.parseDouble(sc.nextLine()));
                        teacherManager.addTeacher(t);
                        break;
                    }
                    case TCH_VIEW:
                        teacherManager.viewAllTeachers();
                        break;
                    case TCH_UPDATE: {
                        System.out.print("ID: ");       String id   = sc.nextLine();
                        System.out.print("New Name: "); String name = sc.nextLine();
                        teacherManager.updateTeacher(id, name);
                        break;
                    }
                    case TCH_DELETE: {
                        System.out.print("ID: ");
                        teacherManager.deleteTeacher(sc.nextLine());
                        break;
                    }
                    case TCH_SEARCH: {
                        System.out.print("ID: ");
                        teacherManager.searchTeacher(sc.nextLine());
                        break;
                    }
                    case TCH_LOAD: {
                        System.out.print("Teacher ID: ");
                        teacherManager.viewTeachingLoad(sc.nextLine());
                        break;
                    }
                    case TCH_REPORT: {
                        System.out.print("ID: ");
                        teacherManager.generateTeacherReport(sc.nextLine());
                        break;
                    }
                    case TCH_SALARY_RPT: {
                        // NEW — OCP: teacherManager.generateSalaryReport() delegates to SalaryReportService
                        System.out.print("Teacher ID: ");
                        teacherManager.generateSalaryReport(sc.nextLine());
                        break;
                    }
                    case TCH_BACK:
                        active = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- TEACHER MODULE ---");
        System.out.println(TCH_ADD        + ". Add Teacher");
        System.out.println(TCH_VIEW       + ". View All Teachers");
        System.out.println(TCH_UPDATE     + ". Update Teacher");
        System.out.println(TCH_DELETE     + ". Delete Teacher");
        System.out.println(TCH_SEARCH     + ". Search Teacher");
        System.out.println(TCH_LOAD       + ". View Teaching Load");
        System.out.println(TCH_REPORT     + ". Teacher Report");
        System.out.println(TCH_SALARY_RPT + ". Salary Report");            // NEW
        System.out.println(TCH_BACK       + ". Back");
        System.out.print("Action: ");
    }

}
