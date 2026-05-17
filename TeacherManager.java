package teacher;

import common.IRepository;
import common.ISalaryStrategy;

import java.util.List;
import java.util.Optional;

/**
 * SRP (Single Responsibility Principle):
 * TeacherManager teacher-related use cases coordinate karta hai — add, update,
 * delete, search, teaching load, aur report. Asli kaam IRepository,
 * TeacherDisplayService, aur TeacherReportService ko delegate hota hai.
 *
 * DIP (Dependency Inversion Principle):
 * IRepository<ITeacher, String> inject hoti hai — storage implementation
 * se decoupled hai. SalaryService bhi inject hoti hai.
 *
 * OCP (Open/Closed Principle):
 * Naya teacher operation add karna ho to naya method dalo — existing methods
 * modify nahi honge.
 *
 * CODE SMELL REMOVED — Duplicate Code (DRY):
 * findOrThrow() private helper — findById + orElseThrow ek jagah.
 */
public class TeacherManager {

    private final IRepository<ITeacher, String> repository;
    private final SalaryService                 salaryService;
    private final ISalaryStrategy               salaryStrategy;

    /**
     * CODE SMELL REMOVED — Null passed as valid state (Special Case / Null smell):
     * Pehle ek 2-arg constructor tha jo null salaryStrategy pass karta tha,
     * aur phir generateSalaryReport() mein runtime null-check karna padta tha.
     * Yeh "pass null as a flag" anti-pattern hai (Refactoring Guru: Null checks
     * / Special Case smell).
     *
     * FIX: Sirf ek constructor — ISalaryStrategy required dependency hai.
     * Main.java mein salaryStrategy pehle se available hai, isliye inject karna
     * koi extra kaam nahi. Ab null kabhi possible nahi — runtime guard hata di.
     */
    public TeacherManager(IRepository<ITeacher, String> repository,
                          SalaryService salaryService,
                          ISalaryStrategy salaryStrategy) {
        if (salaryStrategy == null) throw new IllegalArgumentException("salaryStrategy must not be null");
        this.repository     = repository;
        this.salaryService  = salaryService;
        this.salaryStrategy = salaryStrategy;
    }

    /** Naya teacher repository mein add karta hai. */
    public void addTeacher(ITeacher teacher) {
        repository.add(teacher);
        System.out.println("Teacher added: " + teacher.getName());
    }

    /** Saare teachers ki brief listing print karta hai. */
    public void viewAllTeachers() {
        List<ITeacher> all = repository.findAll();
        if (all.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }
        all.forEach(t ->
            System.out.println(t.getId() + " | " + t.getName() + " | " + t.getDepartment())
        );
    }

    /** Teacher ka naam update karta hai. */
    public void updateTeacher(String id, String newName) {
        findOrThrow(id).setName(newName);
        System.out.println("Teacher updated.");
    }

    /** Teacher ko repository se remove karta hai. */
    public void deleteTeacher(String id) {
        repository.remove(id);
        System.out.println("Teacher deleted.");
    }

    /**
     * Teacher ki full detail display karta hai.
     * Display TeacherDisplayService ko delegate — SRP maintain hoti hai.
     */
    public void searchTeacher(String id) {
        new TeacherDisplayService(findOrThrow(id)).display();
    }

    /** Teacher ka course load (assigned courses list) print karta hai. */
    public void viewTeachingLoad(String id) {
        ITeacher t = findOrThrow(id);
        System.out.println("Teaching load for " + t.getName() + ": " + t.getAssignedCourses());
    }

    /**
     * Teacher ka comprehensive report generate karta hai — salary included.
     * Report TeacherReportService ko delegate — SRP maintain hoti hai.
     */
    public void generateTeacherReport(String id) {
        new TeacherReportService(salaryService).print(findOrThrow(id));
    }

    /**
     * Teacher ka detailed salary report generate karta hai.
     * Null check hatai di — constructor mein enforce hai (ISalaryStrategy required).
     */
    public void generateSalaryReport(String id) {
        new SalaryReportService(salaryStrategy).print(findOrThrow(id));
    }

    /**
     * Optional<ITeacher> expose karta hai — CourseMenuHandler ke liye
     * jo course-teacher assignment karta hai.
     */
    public Optional<ITeacher> findById(String id) {
        return repository.findById(id);
    }

    // ---- private helper ----

    /**
     * CODE SMELL REMOVED — Duplicate Code:
     * findById + orElseThrow pattern ek jagah — DRY principle.
     */
    private ITeacher findOrThrow(String id) {
        return repository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + id));
    }
}
