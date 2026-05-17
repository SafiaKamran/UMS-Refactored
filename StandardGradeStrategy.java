package common;

/**
 * SRP (Single Responsibility Principle):
 * Sirf grade calculate karne ki zimmedari — student data ya display se koi
 * connection nahi.
 *
 * OCP (Open/Closed Principle):
 * Naya grading scale chahiye? Sirf naya IGradeStrategy banao (jaise
 * GpaGradeStrategy) — yeh class ya koi aur class modify nahi hogi.
 *
 * CODE SMELL REMOVED — Magic Numbers:
 * Saare threshold values named constants mein extract kiye gaye hain.
 * Pehle agar 80 likha hota code mein to samajhna mushkil tha ke yeh kya hai.
 */
public class StandardGradeStrategy implements IGradeStrategy {

    // Grade thresholds — percentage marks
    private static final int A_PLUS_MIN = 90;
    private static final int A_MIN      = 80;
    private static final int B_MIN      = 70;
    private static final int C_MIN      = 60;
    private static final int D_MIN      = 50;

    /**
     * Marks ke basis par letter grade return karta hai.
     *
     * CODE SMELL REMOVED — Replace Nested Conditionals with Guard Clauses:
     * Har condition clearly ek grade boundary guard kar rahi hai —
     * deeply nested if-else chain nahi hai.
     */
    @Override
    public String calculate(int marks) {
        if (marks >= A_PLUS_MIN) return "A+";
        if (marks >= A_MIN)      return "A";
        if (marks >= B_MIN)      return "B";
        if (marks >= C_MIN)      return "C";
        if (marks >= D_MIN)      return "D";
        return "F";
    }
}
