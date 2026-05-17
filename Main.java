package main;

import common.*;
import course.*;
import student.*;
import teacher.*;

import java.util.Scanner;

/**
 * SRP (Single Responsibility Principle):
 * Main class ki zimmedari sirf system startup, login, aur main menu handle
 * karna hai. Sub-modules ke kaam unke handlers karte hain.
 *
 * DIP (Dependency Inversion Principle) — Composition Root Pattern:
 * main() method mein saari dependencies build hoti hain (Composition Root).
 * Yeh ek jagah hai jahan concrete classes banai jaati hain — baaki poora
 * system abstractions se kaam karta hai.
 *
 * OCP (Open/Closed Principle):
 * Naya module add karna ho (jaise LibraryMenuHandler)? Main loop mein ek
 * case aur menu mein ek line — existing code largely untouched.
 *
 * CODE SMELL REMOVED — Duplicate Code (readInt):
 * readInt() method charo menu classes mein copy-paste tha. Ab MenuUtils.readInt(sc)
 * use hota hai — ek jagah change karo, sab jagah lagoo hoga.
 *
 * CODE SMELL REMOVED — God Class / Large Class:
 * Pehle agar sab logic ek jagah hota to Main god class ban jata.
 * StudentMenuHandler, TeacherMenuHandler, CourseMenuHandler mein split
 * karke is smell ko avoid kiya gaya hai.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * Main menu choices named constants mein.
 */
public class Main {

    // Main menu option constants
    private static final int MAIN_STUDENT = 1;
    private static final int MAIN_TEACHER = 2;
    private static final int MAIN_COURSE  = 3;
    private static final int MAIN_EXIT    = 4;

    private final Scanner            sc;
    private final IAuthService       auth;
    private final StudentMenuHandler studentHandler;
    private final TeacherMenuHandler teacherHandler;
    private final CourseMenuHandler  courseHandler;

    public Main(Scanner sc, IAuthService auth,
                StudentMenuHandler studentHandler,
                TeacherMenuHandler teacherHandler,
                CourseMenuHandler  courseHandler) {
        this.sc             = sc;
        this.auth           = auth;
        this.studentHandler = studentHandler;
        this.teacherHandler = teacherHandler;
        this.courseHandler  = courseHandler;
    }

    /**
     * Composition Root — saari dependencies yahan wire hoti hain.
     *
     * DIP: Har jagah interfaces use hote hain, sirf yahan concrete classes
     * banai jaati hain. Agar InMemoryRepository ko DatabaseRepository se
     * replace karna ho, sirf yahan change karo.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Strategy objects — OCP: naya strategy inject karo, kuch aur nahi badlega
        IGradeStrategy  gradeStrategy  = new StandardGradeStrategy();
        ISalaryStrategy salaryStrategy = new StandardSalaryStrategy();
        IAuthService    auth           = new SimpleAuthService();

        // Repositories — DIP: managers repositories ke interface se kaam karte hain
        IRepository<IStudent, String>  studentRepo = new InMemoryRepository<>(IStudent::getId);
        IRepository<ITeacher, String>  teacherRepo = new InMemoryRepository<>(ITeacher::getId);
        IRepository<ICourse,  Integer> courseRepo  = new InMemoryRepository<>(ICourse::getId);

        // Services aur managers — saari dependencies inject ki gayi hain
        StudentManager sm = new StudentManager(studentRepo, gradeStrategy);
        SalaryService  ss = new SalaryService(salaryStrategy);
        // OCP: Extended constructor — salaryStrategy inject kiya so SalaryReportService kaam kare
        TeacherManager tm = new TeacherManager(teacherRepo, ss, salaryStrategy);
        CourseManager  cm = new CourseManager(courseRepo);

        // Menu handlers — console UI layer
        StudentMenuHandler studentHandler = new StudentMenuHandler(sc, sm);
        TeacherMenuHandler teacherHandler = new TeacherMenuHandler(sc, tm);
        CourseMenuHandler  courseHandler  = new CourseMenuHandler(sc, cm, tm, sm);

        new Main(sc, auth, studentHandler, teacherHandler, courseHandler).run();
        sc.close();
    }

    /** System ka main loop — login ke baad module routing. */
    private void run() {
        printBanner();
        if (!login()) return; // Authentication fail — exit

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = MenuUtils.readInt(sc);
            switch (choice) {
                case MAIN_STUDENT: studentHandler.run(); break;
                case MAIN_TEACHER: teacherHandler.run(); break;
                case MAIN_COURSE:  courseHandler.run();  break;
                case MAIN_EXIT:
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * User ko authenticate karta hai.
     * Auth logic IAuthService ko delegate — SRP + DIP.
     */
    private boolean login() {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (auth.authenticate(user, pass)) {
            System.out.println("\nLogin successful! Welcome to the dashboard.\n");
            return true;
        }
        System.out.println("Invalid credentials. Exiting.");
        return false;
    }

    private void printBanner() {
        System.out.println("******************************************");
        System.out.println("    UNIVERSITY MANAGEMENT SYSTEM LOGIN    ");
        System.out.println("******************************************");
    }

    private void printMainMenu() {
        System.out.println("\n====== MAIN DASHBOARD ======");
        System.out.println(MAIN_STUDENT + ". Student Module");
        System.out.println(MAIN_TEACHER + ". Teacher Module");
        System.out.println(MAIN_COURSE  + ". Course Module");
        System.out.println(MAIN_EXIT    + ". Exit");
        System.out.print("Choose: ");
    }

}
