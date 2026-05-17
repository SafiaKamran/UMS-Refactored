package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * SRP (Single Responsibility Principle):
 * Is class ki sirf ek zimmedari hai — in-memory collection mein entities store
 * aur retrieve karna. Koi business logic nahi hai yahan.
 *
 * OCP (Open/Closed Principle):
 * Generic type parameters T aur ID ki wajah se yeh class Student, Teacher,
 * aur Course — teeno ke liye bina modification ke kaam karti hai.
 *
 * LSP (Liskov Substitution Principle):
 * IRepository<T,ID> ka full contract implement kiya gaya hai — har jagah
 * IRepository use karne par yeh seamlessly kaam karti hai.
 *
 * CODE SMELL REMOVED — Duplicate Code:
 * Pehle har entity ke liye alag ArrayList aur loop likhe jaate the.
 * Ab ek hi generic class sabke liye use ho rahi hai.
 */
public class InMemoryRepository<T, ID> implements IRepository<T, ID> {

    private final List<T>            store;
    private final Function<T, ID>    idExtractor;

    /**
     * @param idExtractor  entity se uska unique ID nikalne ka function
     *                     (jaise IStudent::getId)
     */
    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.store       = new ArrayList<>();
        this.idExtractor = idExtractor;
    }

    @Override
    public void add(T entity) {
        store.add(entity);
    }

    @Override
    public void remove(ID id) {
        // CODE SMELL REMOVED — Temporary Variable (unnecessary local variable avoided)
        store.removeIf(e -> idExtractor.apply(e).equals(id));
    }

    @Override
    public Optional<T> findById(ID id) {
        return store.stream()
                    .filter(e -> idExtractor.apply(e).equals(id))
                    .findFirst();
    }

    @Override
    public List<T> findAll() {
        // Defensive copy — caller mutation se internal state protect hoti hai
        return new ArrayList<>(store);
    }
}
