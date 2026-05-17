package course;

/**
 * SRP (Single Responsibility Principle):
 * CourseType enum course delivery mode aur uski room requirement
 * encapsulate karta hai — yeh data Course class ya elsewhere scattered nahi.
 *
 * OCP (Open/Closed Principle):
 * Naya delivery mode add karna ho (jaise INTENSIVE) to sirf enum mein
 * ek entry dalo — koi existing switch/if modify nahi hoga.
 *
 * CODE SMELL REMOVED — Switch Statements smell:
 * Room requirement enum mein hi stored hai — baar baar switch(courseType)
 * likhne ki zaroorat nahi padti.
 */
public enum CourseType {

    ONLINE ("No room required — online"),
    OFFLINE("Room required: capacity 30"),
    HYBRID ("Room required: capacity 15 + online slots");

    private final String roomRequirement;

    CourseType(String roomRequirement) {
        this.roomRequirement = roomRequirement;
    }

    /** Course delivery mode ke liye room ki zaroorat describe karta hai. */
    public String getRoomRequirement() {
        return roomRequirement;
    }
}
