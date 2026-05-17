package student;

import common.IGradeStrategy;
import common.IRepository;

import java.util.List;
import java.util.Optional;

/**
 * SRP (Single Responsibility Principle):
 * StudentManager ki sirf ek zimmedari hai — student-related use cases
 * orchestrate karna (add, update, delete, search, enroll, display).
 * Data store karna IRepository ka kaam hai, display karna StudentDisplayService
 * ka kaam hai, grade calculate karna IGradeStrategy ka kaam hai.
 *
 * DIP (Dependency Inversion Principle):
 * Constructor mein IRepository<IStudent, String> aur IGradeStrategy inject hote
 * hain — concrete implementations par dependency nahi. Testing mein mock inject
 * karna asaan hai.
 *
 * OCP (Open/Closed Principle):
 * Naya storage backend ya grading scheme chahiye? Sirf naya implementation
 * inject karo — StudentManager mein koi change nahi.
 *
 * CODE SMELL REMOVED — Middle Man:
 * findOrThrow() private helper use kiya — har public method mein same
 * orElseThrow() pattern repeat nahi hota (DRY).
 */
public class StudentManager {

    private final IRepository<IStudent, String> repository;
    private final IGradeStrategy                gradeStrategy;

    public StudentManager(IRepository<IStudent, String> repository,
                          IGradeStrategy gradeStrategy) {
        this.repository    = repository;
        this.gradeStrategy = gradeStrategy;
    }

    /** Naya student create karke repository mein add karta hai. */
    public void add(String id, String name) {
        // Student construction validation Student class mein hai (SRP)
        repository.add(new Student(id, name));
        System.out.println("Student added: " + name);
    }

    /** Saare students ki brief listing print karta hai. */
    public void view() {
        List<IStudent> all = repository.findAll();
        if (all.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        // CODE SMELL REMOVED — Long Method: forEach lambda use kiya instead of loop
        all.forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
    }

    /** Student ka naam update karta hai. */
    public void update(String id, String newName) {
        findOrThrow(id).setName(newName);
        System.out.println("Student updated.");
    }

    /** Student ko repository se remove karta hai. */
    public void delete(String id) {
        repository.remove(id);
        System.out.println("Student deleted.");
    }

    /** Student ID se dhundh ke brief info print karta hai. */
    public void search(String id) {
        IStudent s = findOrThrow(id);
        System.out.println(s.getId() + " | " + s.getName());
    }

    /** Student ko ek course mein enroll karta hai. */
    public void enrollCourse(String id, String course) {
        findOrThrow(id).addCourse(course);
        System.out.println("Enrolled in: " + course);
    }

    /** Student ke enrolled courses print karta hai. */
    public void viewCourses(String id) {
        System.out.println("Courses: " + findOrThrow(id).getCourses());
    }

    /**
     * Student ki full detail display karta hai.
     * Display StudentDisplayService ko delegate kiya — SRP maintain hoti hai.
     */
    public void studentDetails(String id) {
        IStudent s = findOrThrow(id);
        new StudentDisplayService(s, gradeStrategy).display();
    }

    /**
     * Student ka full academic report generate karta hai.
     * OCP: Naya feature — StudentReportService naya class hai,
     * StudentManager ya koi existing class modify nahi hui.
     *
     * Report mein marks, grade (strategy se), status, aur courses sab hain.
     */
    public void generateReport(String id) {
        IStudent s = findOrThrow(id);
        new StudentReportService(gradeStrategy).print(s);
    }

    /**
     * Optional<IStudent> expose karta hai — CourseMenuHandler jaise
     * callers ke liye jo manual lookup karte hain.
     */
    public Optional<IStudent> findById(String id) {
        return repository.findById(id);
    }

    // ---- private helper ----

    /**
     * CODE SMELL REMOVED — Duplicate Code:
     * Ek jagah findById + orElseThrow logic — sab public methods is helper
     * ko call karte hain. Agar error message change karna ho to ek jagah
     * change karo.
     */
    private IStudent findOrThrow(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }
}
