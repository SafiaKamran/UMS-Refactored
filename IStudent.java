package student;

import java.util.List;

/**
 * ISP (Interface Segregation Principle):
 * IStudent mein sirf wo methods hain jo student entity ke core data aur
 * behaviour se related hain. Display logic aur grade logic alag classes mein
 * hai — interface chota aur focused raha.
 *
 * DIP (Dependency Inversion Principle):
 * StudentManager aur StudentDisplayService is abstraction par depend karte
 * hain — Student concrete class par nahi. Test mein mock banana asaan hai.
 */
public interface IStudent {
    String       getId();
    String       getName();
    String       getEmail();
    int          getMarks();
    List<String> getCourses();

    void setName(String name);
    void setEmail(String email);
    void setMarks(int marks);
    void addCourse(String course);
}
