package course;

import common.IDisplayable;

/**
 * SRP (Single Responsibility Principle):
 * Sirf course information console par print karna — data mutation ya
 * business logic yahan bilkul nahi.
 *
 * DIP (Dependency Inversion Principle):
 * ICourse abstraction par depend karta hai — Course concrete class par nahi.
 *
 * CODE SMELL REMOVED — Feature Envy:
 * CourseManager mein pehle koi display logic hoti agar yeh class na hoti.
 * Ab display ki zimmedari clearly yahan centralized hai.
 */
public class CourseDisplayService implements IDisplayable {

    private final ICourse course;

    public CourseDisplayService(ICourse course) {
        this.course = course;
    }

    @Override
    public void display() {
        System.out.println("===== COURSE DETAILS =====");
        System.out.println("ID       : " + course.getId());
        System.out.println("Course   : " + course.getName());
        System.out.println("Type     : " + course.getCourseType());
        System.out.println("Fee      : " + course.getFee());
        // Room requirement CourseType enum se aata hai — switch nahi likhni (OCP)
        System.out.println("Room Req : " + course.getCourseType().getRoomRequirement());

        if (course.getAssignedTeacher() != null) {
            System.out.println("Teacher  : " + course.getAssignedTeacher().getName()
                    + " | Dept: " + course.getAssignedTeacher().getDepartment());
        } else {
            System.out.println("Teacher  : Not Assigned");
        }

        System.out.println("Students : " + course.getEnrolledStudents().size());
    }
}
