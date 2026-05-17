package common;

/**
 * SRP (Single Responsibility Principle):
 * Sirf net salary computation aur breakdown ki zimmedari — teacher ya course
 * data se directly koi kaam nahi.
 *
 * OCP (Open/Closed Principle):
 * Agar tax rate change ho ya performance bonus add karna ho, sirf naya
 * ISalaryStrategy implement karo — existing code untouched rahega.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * TAX_RATE, COURSE_BONUS, BONUS_THRESHOLD sab named constants hain.
 *
 * CODE SMELL REMOVED — Duplicate Code (DRY violation):
 * Pehle calculate() aur breakdown() dono mein same tax/bonus logic tha.
 * Ab computeBonus() aur computeTax() private helpers mein extract kiya —
 * ek jagah change hoga to dono jagah lagoo hoga.
 */
public class StandardSalaryStrategy implements ISalaryStrategy {

    private static final double TAX_RATE        = 0.10;   // 10% income tax
    private static final double COURSE_BONUS    = 5_000.0; // bonus per extra course
    private static final int    BONUS_THRESHOLD = 2;       // courses > threshold pe bonus milega

    @Override
    public double calculate(double grossSalary, int courseCount) {
        return grossSalary - computeTax(grossSalary) + computeBonus(courseCount);
    }

    @Override
    public String breakdown(double grossSalary, int courseCount) {
        double tax   = computeTax(grossSalary);
        double bonus = computeBonus(courseCount);
        double net   = grossSalary - tax + bonus;

        return String.format(
            "Gross: %.2f | Tax (10%%): %.2f | Bonus: %.2f | Net: %.2f",
            grossSalary, tax, bonus, net
        );
    }

    // ---- private helpers ----

    /** Gross salary ka tax amount compute karta hai. */
    private double computeTax(double grossSalary) {
        return grossSalary * TAX_RATE;
    }

    /**
     * Course count ke hisaab se bonus decide karta hai.
     * Bonus sirf tab milta hai jab courses BONUS_THRESHOLD se zyada hon.
     */
    private double computeBonus(int courseCount) {
        return courseCount > BONUS_THRESHOLD ? COURSE_BONUS : 0;
    }
}
