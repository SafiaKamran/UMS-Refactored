package teacher;

/**
 * SRP (Single Responsibility Principle):
 * TeacherReportService ki sirf ek zimmedari — teacher ka comprehensive report
 * print karna jisme salary breakdown bhi ho. Display-only class hai.
 *
 * DIP (Dependency Inversion Principle):
 * SalaryService inject hoti hai — net salary compute karne ke liye
 * khud koi calculation nahi karta.
 *
 * CODE SMELL REMOVED — Feature Envy:
 * TeacherManager mein report print karne ki koi bhi logic nahi rahi —
 * sab kuch yahan centralized hai.
 */
public class TeacherReportService {

    private final SalaryService salaryService;

    public TeacherReportService(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    /**
     * Teacher ka full report print karta hai — identity, department,
     * gross salary, net salary, aur course load sab mila ke.
     */
    public void print(ITeacher teacher) {
        System.out.println("======= TEACHER REPORT =======");
        System.out.println("ID         : " + teacher.getId());
        System.out.println("Name       : " + teacher.getName());
        System.out.println("Department : " + teacher.getDepartment());
        System.out.println("Gross      : " + teacher.getSalary());
        // Net salary SalaryService se compute hota hai — strategy pattern (OCP)
        System.out.println("Net Salary : " + salaryService.computeNet(teacher));
        System.out.println("Courses    : " + teacher.getAssignedCourses());
        System.out.println("Load Count : " + teacher.getAssignedCourses().size());
        System.out.println("==============================");
    }
}
