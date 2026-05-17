package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SRP (Single Responsibility Principle):
 * Student class sirf student ki data hold karne aur validate karne ka kaam
 * karti hai. Grade calculation, display, aur persistence — yeh sab alag
 * classes mein hain.
 *
 * LSP (Liskov Substitution Principle):
 * IStudent ka pura contract implement kiya gaya hai — koi method silently
 * kuch different nahi karta. Har jagah IStudent chahiye wahan Student
 * safely use ho sakta hai.
 *
 * CODE SMELL REMOVED — Data Class smell partially:
 * Pure getter/setter se aage, validation constructors aur setters mein
 * embed ki gayi hai — invalid state possible nahi hai.
 */
public class Student implements IStudent {

    // Default email jab student register kare aur email na do
    private static final String DEFAULT_EMAIL = "";

    private final String       id;
    private       String       name;
    private       String       email;
    private       int          marks;
    private final List<String> courses = new ArrayList<>();

    /**
     * CODE SMELL REMOVED — Constructor Complexity:
     * Validation constructor mein hi hai — object kabhi invalid state mein
     * nahi banta. Guard clauses use kiye hain nested if se bachne ke liye.
     */
    public Student(String id, String name) {
        if (id   == null || id.isBlank())   throw new IllegalArgumentException("Student id must not be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Student name must not be blank");
        this.id    = id.trim();
        this.name  = name.trim();
        this.email = DEFAULT_EMAIL;
    }

    @Override public String getId()    { return id; }
    @Override public String getName()  { return name; }
    @Override public String getEmail() { return email; }
    @Override public int    getMarks() { return marks; }

    /**
     * CODE SMELL REMOVED — Inappropriate Intimacy:
     * Unmodifiable list return karta hai — caller internal list change
     * nahi kar sakta (encapsulation preserved).
     */
    @Override
    public List<String> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name must not be blank");
        this.name = name.trim();
    }

    /**
     * Basic email format check — sirf '@' presence verify karta hai.
     * Production mein regex-based validator ya dedicated EmailAddress value object use karo.
     */
    @Override
    public void setEmail(String email) {
        if (email != null && !email.isBlank() && !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address: " + email);
        this.email = (email == null) ? DEFAULT_EMAIL : email.trim();
    }

    @Override
    public void setMarks(int marks) {
        if (marks < 0 || marks > 100)
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        this.marks = marks;
    }

    /**
     * Duplicate enrollment prevent karta hai — contains() check se.
     * CODE SMELL REMOVED — Speculative Generality: list mein ek hi course
     * ek baar hona chahiye, yeh rule yahan enforce hoti hai.
     */
    @Override
    public void addCourse(String course) {
        if (course == null || course.isBlank())
            throw new IllegalArgumentException("Course name must not be blank");
        String trimmed = course.trim();
        if (!courses.contains(trimmed)) {
            courses.add(trimmed);
        }
    }
}
