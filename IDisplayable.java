package common;

/**
 * ISP (Interface Segregation Principle):
 * Alag display responsibility ke liye dedicated interface —
 * classes sirf tab implement karti hain jab unhe display karna ho.
 */
public interface IDisplayable {
    void display();
}
