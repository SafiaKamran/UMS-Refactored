package course;

import java.util.List;

/**
 * OCP (Open/Closed Principle):
 * Course module mein "Fee Report" feature add kiya gaya hai bina
 * CourseManager, Course, ya CourseDisplayService ko modify kiye.
 * Sirf yeh naya class banaya — existing code completely untouched.
 * Extension allowed, modification closed.
 *
 * SRP (Single Responsibility Principle):
 * CourseFeeReportService ki ek hi zimmedari — courses ki fee summary
 * report generate karna. Revenue calculation, enrollment stats, aur
 * fee totals sab yahan centralized hain.
 *
 * DIP (Dependency Inversion Principle):
 * ICourse abstraction par depend karta hai — Course concrete class ko
 * directly nahi janta. List<ICourse> injection ke zariye kaam karta hai.
 *
 * ISP (Interface Segregation Principle):
 * Fee reporting ek focused concern hai — CourseDisplayService (jo ek
 * course ki detail show karta hai) se clearly alag rakhा gaya.
 */
public class CourseFeeReportService {

    /**
     * Saare courses ki fee summary report console par print karta hai.
     * Har course ki fee, enrolled students, aur expected revenue dikhata hai.
     * Summary mein total courses, total enrolled students, aur grand total
     * expected revenue shamil hain.
     *
     * @param courses  report jis course list ke liye generate hogi
     *                 (IRepository.findAll() ki return value inject karo)
     */
    public void print(List<ICourse> courses) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║              COURSE FEE REPORT                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            System.out.println("══════════════════════════════════════════════════════");
            return;
        }

        // Column headers
        System.out.printf("%-4s  %-20s  %-8s  %10s  %8s  %14s%n",
                "ID", "Course Name", "Type", "Fee (PKR)", "Students", "Revenue (PKR)");
        System.out.println("------------------------------------------------------");

        double grandTotalRevenue = 0.0;
        int    grandTotalStudents = 0;

        for (ICourse course : courses) {
            int    studentCount     = course.getEnrolledStudents().size();
            double expectedRevenue  = course.getFee() * studentCount;

            grandTotalRevenue  += expectedRevenue;
            grandTotalStudents += studentCount;

            System.out.printf("%-4d  %-20s  %-8s  %10.2f  %8d  %14.2f%n",
                    course.getId(),
                    truncate(course.getName(), 20),
                    course.getCourseType().name(),
                    course.getFee(),
                    studentCount,
                    expectedRevenue);
        }

        System.out.println("------------------------------------------------------");
        System.out.printf("Total Courses   : %d%n",          courses.size());
        System.out.printf("Total Students  : %d%n",          grandTotalStudents);
        System.out.printf("Grand Revenue   : PKR %,.2f%n",   grandTotalRevenue);

        // Fee distribution by course type
        // CODE SMELL REMOVED — Duplicate Iteration (Refactoring Guru: loops smell):
        // Pehle har CourseType ke liye list do baar traverse hoti thi —
        // ek count() ke liye, ek sum() ke liye. Ab ek hi pass mein
        // Map<CourseType, long[]> banate hain: [0]=count, [1]=revenue.
        System.out.println();
        System.out.println("  Fee by Course Type:");
        java.util.Map<CourseType, long[]> typeStats = new java.util.EnumMap<>(CourseType.class);
        for (ICourse c : courses) {
            typeStats.computeIfAbsent(c.getCourseType(), k -> new long[2]);
            typeStats.get(c.getCourseType())[0]++;                                       // count
            typeStats.get(c.getCourseType())[1] += (long)(c.getFee() * c.getEnrolledStudents().size()); // revenue (scaled)
        }
        // EnumMap iteration order matches CourseType declaration order
        for (CourseType type : CourseType.values()) {
            long[] stats = typeStats.get(type);
            if (stats != null) {
                System.out.printf("  %-8s : %d courses | Revenue: PKR %,.2f%n",
                        type.name(), stats[0], (double) stats[1]);
            }
        }
        System.out.println("══════════════════════════════════════════════════════");
    }

    // ---- private helper ----

    /**
     * Long string ko max length par truncate karta hai display ke liye.
     * CODE SMELL REMOVED — Inline string operation ko named method mein extract kiya.
     */
    private String truncate(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}
