package common;

/**
 * OCP + SRP (Open/Closed & Single Responsibility Principle):
 * Salary calculation ek alag strategy hai — naya tax ya bonus rule add karna ho to
 * sirf naya ISalaryStrategy implement karo, TeacherManager ya SalaryService ko
 * chhona nahi parega.
 */
public interface ISalaryStrategy {
    double calculate(double grossSalary, int courseCount);
    String breakdown(double grossSalary, int courseCount);
}
