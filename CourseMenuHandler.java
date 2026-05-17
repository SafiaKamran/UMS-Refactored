package main;

import course.Course;
import course.CourseManager;
import course.CourseType;
import student.IStudent;
import student.StudentManager;
import teacher.ITeacher;
import teacher.TeacherManager;

import java.util.Scanner;

/**
 * SRP (Single Responsibility Principle):
 * Course module ka console UI handle karta hai — user input routing.
 * Business logic CourseManager mein hai, teacher/student lookup unke
 * managers mein.
 *
 * DIP (Dependency Inversion Principle):
 * CourseManager, TeacherManager, StudentManager — sab inject hote hain.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * Saare menu choices named constants mein.
 *
 * CODE SMELL REMOVED — Long Method (run()):
 * Har case mein sirf input + delegate — koi complex logic nahi.
 */
public class CourseMenuHandler {

    private static final int CRS_ADD      = 1;
    private static final int CRS_VIEW     = 2;
    private static final int CRS_UPDATE   = 3;
    private static final int CRS_DELETE   = 4;
    private static final int CRS_SEARCH   = 5;
    private static final int CRS_TEACHER  = 6;
    private static final int CRS_STUDENT  = 7;
    private static final int CRS_DETAILS  = 8;
    private static final int CRS_FEE_RPT  = 9;   // NEW — OCP: existing constants untouched
    private static final int CRS_BACK     = 10;

    private final Scanner        sc;
    private final CourseManager  courseManager;
    private final TeacherManager teacherManager;
    private final StudentManager studentManager;

    public CourseMenuHandler(Scanner sc, CourseManager courseManager,
                             TeacherManager teacherManager, StudentManager studentManager) {
        this.sc             = sc;
        this.courseManager  = courseManager;
        this.teacherManager = teacherManager;
        this.studentManager = studentManager;
    }

    /** Course module ka main loop — jab tak user "Back" na choose kare. */
    public void run() {
        boolean active = true;
        while (active) {
            printMenu();
            int ch = MenuUtils.readInt(sc);
            try {
                switch (ch) {
                    case CRS_ADD: {
                        System.out.print("ID: ");   int    id   = Integer.parseInt(sc.nextLine());
                        System.out.print("Name: "); String name = sc.nextLine();
                        Course c = new Course(id, name);
                        System.out.print("Type (ONLINE/OFFLINE/HYBRID): ");
                        c.setCourseType(CourseType.valueOf(sc.nextLine().trim().toUpperCase()));
                        System.out.print("Fee: ");
                        c.setFee(Double.parseDouble(sc.nextLine()));
                        courseManager.addCourse(c);
                        break;
                    }
                    case CRS_VIEW:
                        courseManager.viewAllCourses();
                        break;
                    case CRS_UPDATE: {
                        System.out.print("ID: ");   int    id   = Integer.parseInt(sc.nextLine());
                        System.out.print("Name: "); String name = sc.nextLine();
                        courseManager.updateCourse(id, name);
                        break;
                    }
                    case CRS_DELETE: {
                        System.out.print("ID: ");
                        courseManager.deleteCourse(Integer.parseInt(sc.nextLine()));
                        break;
                    }
                    case CRS_SEARCH: {
                        System.out.print("ID: ");
                        courseManager.searchCourse(Integer.parseInt(sc.nextLine()));
                        break;
                    }
                    case CRS_TEACHER: {
                        System.out.print("Course ID: ");  int    cid = Integer.parseInt(sc.nextLine());
                        System.out.print("Teacher ID: "); String tid = sc.nextLine();
                        // Teacher lookup TeacherManager se — CourseManager ka kaam nahi
                        ITeacher t = teacherManager.findById(tid)
                                .orElseThrow(() -> new IllegalArgumentException("Teacher not found."));
                        courseManager.assignTeacherToCourse(cid, t);
                        break;
                    }
                    case CRS_STUDENT: {
                        System.out.print("Course ID: ");  int    cid = Integer.parseInt(sc.nextLine());
                        System.out.print("Student ID: "); String sid = sc.nextLine();
                        // Student lookup StudentManager se — CourseManager ka kaam nahi
                        IStudent s = studentManager.findById(sid)
                                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
                        courseManager.enrollStudentInCourse(cid, s);
                        break;
                    }
                    case CRS_DETAILS: {
                        System.out.print("Course ID: ");
                        courseManager.viewCourseDetails(Integer.parseInt(sc.nextLine()));
                        break;
                    }
                    case CRS_FEE_RPT:
                        // NEW — OCP: courseManager.generateFeeReport() delegates to CourseFeeReportService
                        courseManager.generateFeeReport();
                        break;
                    case CRS_BACK:
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
        System.out.println("\n--- COURSE MODULE ---");
        System.out.println(CRS_ADD     + ". Add Course");
        System.out.println(CRS_VIEW    + ". View All Courses");
        System.out.println(CRS_UPDATE  + ". Update Course");
        System.out.println(CRS_DELETE  + ". Delete Course");
        System.out.println(CRS_SEARCH  + ". Search Course");
        System.out.println(CRS_TEACHER + ". Assign Teacher to Course");
        System.out.println(CRS_STUDENT + ". Enroll Student in Course");
        System.out.println(CRS_DETAILS + ". Course Details");
        System.out.println(CRS_FEE_RPT + ". Course Fee Report");           // NEW
        System.out.println(CRS_BACK    + ". Back");
        System.out.print("Action: ");
    }

}
