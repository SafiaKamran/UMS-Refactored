package teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SRP (Single Responsibility Principle):
 * Teacher class sirf teacher ki state hold aur validate karti hai.
 * Salary calculation SalaryService mein, display TeacherDisplayService mein,
 * orchestration TeacherManager mein hai.
 *
 * LSP (Liskov Substitution Principle):
 * ITeacher ka pura contract faithfully implement kiya — koi hidden side-effects
 * nahi, koi silent failures nahi. Koi bhi ITeacher use karne wala
 * safely Teacher object expect kar sakta hai.
 *
 * CODE SMELL REMOVED — Data Class:
 * Validation setters mein hai — invalid data accept nahi hota. Object
 * sirf valid state mein exist karta hai.
 */
public class Teacher implements ITeacher {

    // Department tab tak "Unassigned" hai jab tak explicitly set na ho
    private static final String DEFAULT_DEPARTMENT = "Unassigned";

    private final String       id;
    private       String       name;
    private       String       department;
    private       double       salary;
    private final List<String> assignedCourses = new ArrayList<>();

    /**
     * CODE SMELL REMOVED — Constructor ki Guard Clauses:
     * Blank/null check fail-fast approach se — invalid Teacher object kabhi
     * nahi banta.
     */
    public Teacher(String id, String name) {
        if (id   == null || id.isBlank())   throw new IllegalArgumentException("Teacher id must not be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Teacher name must not be blank");
        this.id         = id.trim();
        this.name       = name.trim();
        this.department = DEFAULT_DEPARTMENT;
        this.salary     = 0.0;
    }

    @Override public String getId()         { return id; }
    @Override public String getName()       { return name; }
    @Override public String getDepartment() { return department; }
    @Override public double getSalary()     { return salary; }

    /**
     * CODE SMELL REMOVED — Inappropriate Intimacy:
     * Unmodifiable list — bahir se internal list tamper nahi ho sakti.
     */
    @Override
    public List<String> getAssignedCourses() {
        return Collections.unmodifiableList(assignedCourses);
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        this.name = name.trim();
    }

    /**
     * Null/blank department se gracefully handle karta hai —
     * default value assign karta hai throw karne ke bajaye.
     */
    @Override
    public void setDepartment(String dept) {
        this.department = (dept == null || dept.isBlank()) ? DEFAULT_DEPARTMENT : dept.trim();
    }

    @Override
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    /**
     * Duplicate course assignment prevent karta hai.
     * CODE SMELL REMOVED — Speculative Generality: ek hi course ek baar assigned hona
     * chahiye — yeh rule yahan enforce hoti hai.
     */
    @Override
    public void assignCourse(String courseName) {
        if (courseName == null || courseName.isBlank())
            throw new IllegalArgumentException("Course name must not be blank");
        String trimmed = courseName.trim();
        if (!assignedCourses.contains(trimmed)) {
            assignedCourses.add(trimmed);
        }
    }
}
