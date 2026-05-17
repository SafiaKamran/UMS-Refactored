package common;

/**
 * OCP + SRP (Open/Closed & Single Responsibility Principle):
 * Grade calculation ki responsibility sirf is interface ke implementors ki hai.
 * Naya grading system add karna ho to sirf naya class banao — existing code change nahi hoga.
 */
public interface IGradeStrategy {
    String calculate(int marks);
}
