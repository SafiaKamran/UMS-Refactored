package main;

import java.util.Scanner;

/**
 * CODE SMELL REMOVED — Duplicate Code (refactoring.guru):
 * readInt() method bilkul same code Main, StudentMenuHandler,
 * TeacherMenuHandler, aur CourseMenuHandler — charo mein copy-paste tha.
 *
 * Fix — Extract Class / Extract Method:
 * Shared logic ek jagah centralize kar diya. Ab agar input handling
 * change karni ho (jaise logging add karni ho, ya trim logic change ho),
 * sirf yahan ek jagah change hoga — charo classes automatically benefit
 * karein gi.
 *
 * SRP (Single Responsibility Principle):
 * Is utility class ki sirf ek zimmedari — console se safe integer input
 * padhna. Koi business logic nahi, koi display nahi.
 *
 * NOTE — Static utility class kyun?
 * readInt() ka koi state nahi — Scanner bahar se aata hai. Is liye
 * static method logical choice hai. Instance banana unnecessary hoga.
 */
public final class MenuUtils {

    // Utility class — instantiation band
    private MenuUtils() {}

    /**
     * Scanner se ek integer line padhta hai.
     * Agar input valid integer nahi hai to -1 return karta hai
     * (safe sentinel value — koi bhi real menu choice -1 nahi hoti).
     *
     * @param sc  shared Scanner instance
     * @return    parsed integer, ya -1 agar invalid input ho
     */
    public static int readInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            // Invalid input ko silently handle karo — caller "Invalid choice" print karega
            return -1;
        }
    }
}
