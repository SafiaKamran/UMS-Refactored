package teacher;

import common.ISalaryStrategy;

/**
 * SRP (Single Responsibility Principle):
 * SalaryService ki zimmedari sirf salary compute karna aur breakdown print
 * karna hai — teacher data manage karna ya store karna nahi.
 *
 * DIP (Dependency Inversion Principle):
 * ISalaryStrategy concrete class pe depend nahi — inject kiya jaata hai.
 * Kal HRA ya provident fund include karna ho to sirf naya strategy banao.
 *
 * OCP (Open/Closed Principle):
 * Naya salary rule? Naya ISalaryStrategy implement karo — SalaryService
 * modify nahi hogi.
 *
 * CODE SMELL REMOVED — Feature Envy:
 * Teacher object se data le kar ISalaryStrategy ko delegate karta hai —
 * strategy mein khud calculation nahi karta.
 */
public class SalaryService {

    private final ISalaryStrategy strategy;

    public SalaryService(ISalaryStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Teacher ki salary ka detailed breakdown print karta hai.
     * Gross salary aur course count teacher se lete hain — strategy ko delegate.
     */
    public void printSalaryBreakdown(ITeacher teacher) {
        System.out.println("=== Salary Breakdown: " + teacher.getName() + " ===");
        System.out.println(
            strategy.breakdown(teacher.getSalary(), teacher.getAssignedCourses().size())
        );
    }

    /**
     * Net salary compute karke return karta hai.
     * TeacherReportService is value ko report mein use karta hai.
     */
    public double computeNet(ITeacher teacher) {
        return strategy.calculate(teacher.getSalary(), teacher.getAssignedCourses().size());
    }
}
