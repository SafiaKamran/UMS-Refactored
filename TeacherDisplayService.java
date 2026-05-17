package teacher;

import common.IDisplayable;

/**
 * SRP (Single Responsibility Principle):
 * Sirf teacher ka basic information display karna — salary calculation ya
 * report generation yahan nahi.
 *
 * DIP (Dependency Inversion Principle):
 * ITeacher abstraction par depend karta hai — Teacher concrete class par nahi.
 *
 * CODE SMELL REMOVED — Feature Envy:
 * Display logic TeacherManager se nikal kar yahan rakh di — TeacherManager
 * ko console output ki detail nahi pata honi chahiye.
 */
public class TeacherDisplayService implements IDisplayable {

    private final ITeacher teacher;

    public TeacherDisplayService(ITeacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void display() {
        System.out.println("=== Teacher Details ===");
        System.out.println("ID         : " + teacher.getId());
        System.out.println("Name       : " + teacher.getName());
        System.out.println("Department : " + teacher.getDepartment());
        System.out.println("Salary     : " + teacher.getSalary());
        System.out.println("Courses    : " + teacher.getAssignedCourses());
    }
}
