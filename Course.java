package course;

import student.IStudent;
import teacher.ITeacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SRP (Single Responsibility Principle):
 * Course class sirf course ki state hold aur validate karti hai.
 * Display CourseDisplayService ka kaam, orchestration CourseManager ka kaam.
 *
 * LSP (Liskov Substitution Principle):
 * ICourse ka pura contract implement kiya — koi silent failure ya unexpected
 * behaviour nahi. Har jagah ICourse chahiye wahan Course safely use hota hai.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * DEFAULT_FEE aur DEFAULT_TYPE named constants hain.
 *
 * CODE SMELL REMOVED — Inappropriate Intimacy:
 * enrolledStudents unmodifiable list ke through expose hoti hai —
 * caller directly list modify nahi kar sakta.
 */
public class Course implements ICourse {

    // Naya course banane par default values
    private static final double     DEFAULT_FEE  = 10_000.0;
    private static final CourseType DEFAULT_TYPE = CourseType.OFFLINE;

    private final int            id;
    private       String         name;
    private       CourseType     courseType;
    private       double         fee;
    private       ITeacher       assignedTeacher;
    private final List<IStudent> enrolledStudents = new ArrayList<>();

    /**
     * CODE SMELL REMOVED — Constructor Guard Clauses:
     * Invalid id ya blank name se immediately fail — invalid Course kabhi
     * nahi banta.
     */
    public Course(int id, String name) {
        if (id <= 0)                        throw new IllegalArgumentException("Course id must be positive");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Course name must not be blank");
        this.id         = id;
        this.name       = name.trim();
        this.courseType = DEFAULT_TYPE;
        this.fee        = DEFAULT_FEE;
    }

    @Override public int        getId()              { return id; }
    @Override public String     getName()            { return name; }
    @Override public CourseType getCourseType()      { return courseType; }
    @Override public double     getFee()             { return fee; }
    @Override public ITeacher   getAssignedTeacher() { return assignedTeacher; }

    @Override
    public List<IStudent> getEnrolledStudents() {
        // Defensive copy — bahir wala code internal list change nahi kar sakta
        return Collections.unmodifiableList(enrolledStudents);
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        this.name = name.trim();
    }

    @Override
    public void setCourseType(CourseType type) {
        if (type == null) throw new IllegalArgumentException("Course type must not be null");
        this.courseType = type;
    }

    @Override
    public void setFee(double fee) {
        if (fee < 0) throw new IllegalArgumentException("Fee cannot be negative");
        this.fee = fee;
    }

    /** Teacher assign karta hai — null se previous assignment clear hoti hai. */
    @Override
    public void setAssignedTeacher(ITeacher teacher) {
        this.assignedTeacher = teacher;
    }

    /**
     * Student enroll karta hai — duplicate enrollment prevent karta hai.
     * CODE SMELL REMOVED — Speculative Generality:
     * contains() check ensures ek student ek hi baar enrolled hota hai.
     */
    @Override
    public void addStudent(IStudent student) {
        if (student == null) throw new IllegalArgumentException("Student must not be null");
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }
}
