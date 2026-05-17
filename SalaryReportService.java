package teacher;

import common.ISalaryStrategy;

/**
 * OCP (Open/Closed Principle):
 * Teacher module mein "Salary Report" feature add kiya gaya hai bina
 * TeacherManager, Teacher, SalaryService, ya TeacherReportService modify kiye.
 * Yeh naya class extension hai — existing code closed for modification.
 *
 * SRP (Single Responsibility Principle):
 * SalaryReportService ki sirf ek zimmedari — teacher ka detailed salary
 * breakdown report generate karna. Existing TeacherReportService (jo basic
 * report deta hai) se clearly alag kiya gaya hai.
 *
 * DIP (Dependency Inversion Principle):
 * ITeacher aur ISalaryStrategy — dono abstractions par depend karta hai.
 * Concrete Teacher ya StandardSalaryStrategy ko directly nahi janta.
 *
 * Yeh class TeacherReportService se alag kyun?
 * TeacherReportService ek general teacher report hai (ID, name, department,
 * net salary, courses). SalaryReportService exclusively salary ka detailed
 * financial breakdown deta hai — gross, tax, bonus, net sab alag alag.
 * Dono different concerns hain — ISP ke mutabiq alag rakhe.
 */
public class SalaryReportService {

    private final ISalaryStrategy salaryStrategy;

    /**
     * @param salaryStrategy  salary compute karne ki strategy
     *                        (OCP: naya tax rule? sirf strategy swap karo)
     */
    public SalaryReportService(ISalaryStrategy salaryStrategy) {
        this.salaryStrategy = salaryStrategy;
    }

    /**
     * Teacher ka full salary report print karta hai.
     *
     * CODE SMELL REMOVED — Duplicate Code (DRY violation):
     * Pehle is class mein TAX_RATE, COURSE_BONUS, BONUS_THRESHOLD constants
     * aur computeTax()/computeBonus() helpers the jo StandardSalaryStrategy se
     * exactly copy-paste the. Yeh ek serious DRY violation tha — agar tax rate
     * change hota to DO jagah update karni padti thi.
     *
     * FIX — ISalaryStrategy ko delegate karo:
     * ISalaryStrategy.breakdown() already sab compute karta hai. Hum sirf
     * us breakdown ko display karte hain aur net() call karte hain — khud
     * koi tax/bonus logic nahi rakhte. Ab koi bhi ISalaryStrategy inject
     * karo, report automatically usi strategy ka result show karegi (OCP bhi
     * satisfy hota hai).
     *
     * @param teacher  jis teacher ki salary report chahiye
     */
    public void print(ITeacher teacher) {
        int    courseCount = teacher.getAssignedCourses().size();
        double gross       = teacher.getSalary();
        // Strategy ko delegate — yahan koi tax/bonus computation nahi (DRY fix)
        double net         = salaryStrategy.calculate(gross, courseCount);
        String breakdown   = salaryStrategy.breakdown(gross, courseCount);

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       TEACHER SALARY REPORT      ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("Teacher ID   : " + teacher.getId());
        System.out.println("Name         : " + teacher.getName());
        System.out.println("Department   : " + teacher.getDepartment());
        System.out.println("Courses Load : " + courseCount + " course(s)");
        System.out.println("--------------------------------------");
        // Breakdown string strategy khud produce karti hai — format bhi strategy ka
        System.out.println("Breakdown    : " + breakdown);
        System.out.println("--------------------------------------");
        System.out.printf ("Net Salary   : PKR %,.2f%n", net);
        System.out.println("══════════════════════════════════════");
    }
}
