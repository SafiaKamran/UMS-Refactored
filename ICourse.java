package course;

import student.IStudent;
import teacher.ITeacher;

import java.util.List;

/**
 * ISP (Interface Segregation Principle):
 * ICourse mein sirf course entity ka essential data aur behaviour — display,
 * management operations alag classes mein.
 *
 * DIP (Dependency Inversion Principle):
 * CourseManager aur CourseDisplayService is abstraction par depend karte hain
 * — concrete Course class directly koi nahi use karta.
 */
public interface ICourse {
    int            getId();
    String         getName();
    CourseType     getCourseType();
    double         getFee();
    ITeacher       getAssignedTeacher();
    List<IStudent> getEnrolledStudents();

    void setName(String name);
    void setCourseType(CourseType type);
    void setFee(double fee);
    void setAssignedTeacher(ITeacher teacher);
    void addStudent(IStudent student);
}
