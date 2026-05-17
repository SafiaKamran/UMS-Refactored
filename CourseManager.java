package course;

import common.IRepository;
import student.IStudent;
import teacher.ITeacher;

import java.util.List;
import java.util.Optional;

/**
 * SRP (Single Responsibility Principle):
 * CourseManager course-related use cases coordinate karta hai.
 * Data store karna IRepository ka kaam, display karna CourseDisplayService ka.
 *
 * DIP (Dependency Inversion Principle):
 * IRepository<ICourse, Integer> inject hoti hai — storage mechanism se
 * completely decoupled. Testing mein mock inject karna asaan hai.
 *
 * OCP (Open/Closed Principle):
 * Naya course operation add karna ho to naya method dalo — existing code
 * modify nahi hoga.
 *
 * CODE SMELL REMOVED — Duplicate Code (DRY):
 * findOrThrow() private helper — findById + orElseThrow ek jagah.
 */
public class CourseManager {

    private final IRepository<ICourse, Integer> repository;

    public CourseManager(IRepository<ICourse, Integer> repository) {
        this.repository = repository;
    }

    /** Naya course repository mein add karta hai. */
    public void addCourse(ICourse course) {
        repository.add(course);
        System.out.println("Course added: " + course.getName());
    }

    /** Saare courses ki brief listing print karta hai. */
    public void viewAllCourses() {
        List<ICourse> all = repository.findAll();
        if (all.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        // CODE SMELL REMOVED — Long Method: forEach lambda concise hai
        all.forEach(c ->
            System.out.println(c.getId() + " | " + c.getName()
                    + " | " + c.getCourseType() + " | Fee: " + c.getFee())
        );
    }

    /** Course ka naam update karta hai. */
    public void updateCourse(int id, String newName) {
        findOrThrow(id).setName(newName);
        System.out.println("Course updated.");
    }

    /** Course ko repository se remove karta hai. */
    public void deleteCourse(int id) {
        repository.remove(id);
        System.out.println("Course deleted.");
    }

    /**
     * Course dhundh ke display karta hai.
     * Display CourseDisplayService ko delegate — SRP.
     */
    public void searchCourse(int id) {
        new CourseDisplayService(findOrThrow(id)).display();
    }

    /**
     * Teacher ko course assign karta hai aur teacher ke record mein bhi
     * course name add karta hai — bidirectional link maintain karta hai.
     */
    public void assignTeacherToCourse(int courseId, ITeacher teacher) {
        ICourse course = findOrThrow(courseId);
        course.setAssignedTeacher(teacher);
        // Teacher ke side par bhi course name track hota hai
        teacher.assignCourse(course.getName());
        System.out.println("Teacher assigned to course.");
    }

    /** Student ko course mein enroll karta hai. */
    public void enrollStudentInCourse(int courseId, IStudent student) {
        findOrThrow(courseId).addStudent(student);
        System.out.println("Student enrolled in course.");
    }

    /**
     * Course ki full detail display karta hai.
     * Display CourseDisplayService ko delegate — SRP.
     */
    public void viewCourseDetails(int id) {
        new CourseDisplayService(findOrThrow(id)).display();
    }

    /**
     * Saare courses ki fee summary report generate karta hai.
     * OCP: Naya feature — CourseFeeReportService naya class hai,
     * CourseManager mein sirf yeh ek method add kiya gaya — koi existing
     * method modify nahi hui.
     */
    public void generateFeeReport() {
        new CourseFeeReportService().print(repository.findAll());
    }

    /**
     * Optional<ICourse> expose karta hai — baahari callers ke liye
     * jo manual lookup karna chahte hain.
     */
    public Optional<ICourse> findById(int id) {
        return repository.findById(id);
    }

    // ---- private helper ----

    /**
     * CODE SMELL REMOVED — Duplicate Code:
     * findById + orElseThrow pattern ek jagah — DRY principle.
     */
    private ICourse findOrThrow(int id) {
        return repository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }
}
