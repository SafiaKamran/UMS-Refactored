package teacher;

import java.util.List;

/**
 * ISP (Interface Segregation Principle):
 * ITeacher mein sirf teacher entity ka core data aur behaviour — salary
 * calculation, report printing, aur display sab alag classes mein.
 *
 * DIP (Dependency Inversion Principle):
 * TeacherManager, SalaryService, TeacherDisplayService — sab is abstraction
 * par depend karte hain. Teacher concrete class directly koi nahi janta.
 */
public interface ITeacher {
    String       getId();
    String       getName();
    String       getDepartment();
    double       getSalary();
    List<String> getAssignedCourses();

    void setName(String name);
    void setDepartment(String dept);
    void setSalary(double salary);
    void assignCourse(String courseName);
}
