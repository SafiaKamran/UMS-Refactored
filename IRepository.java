package common;

import java.util.List;
import java.util.Optional;

/**
 * DIP (Dependency Inversion Principle):
 * High-level modules (Manager classes) depend on this abstraction, concrete
 * storage implementation par nahi. Kal database-backed repository banana ho to
 * sirf naya class banao aur inject karo — koi aur code nahi badlega.
 *
 * OCP (Open/Closed Principle):
 * Naye entity types ke liye generic parameter T aur ID use kiya gaya hai —
 * repository ko modify kiye bina har entity ke liye reuse hoti hai.
 */
public interface IRepository<T, ID> {
    void           add(T entity);
    void           remove(ID id);
    Optional<T>    findById(ID id);
    List<T>        findAll();
}
