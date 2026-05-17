package main;

/**
 * SRP (Single Responsibility Principle):
 * Sirf username/password validate karna — session management ya role check
 * yahan nahi.
 *
 * OCP (Open/Closed Principle):
 * IAuthService implement karta hai — Main class modify kiye bina koi bhi
 * auth mechanism swap ho sakta hai (database, LDAP, OAuth).
 *
 * CODE SMELL REMOVED — Magic Strings:
 * Hardcoded credentials named constants mein hai — code mein raw string
 * "admin" ya "1234" nazar nahi aata.
 *
 * NOTE — Production Warning:
 * Yeh implementation demo/learning ke liye hai. Production mein credentials
 * database mein hashed store hone chahiye aur BCrypt jaise algorithm se
 * compare kiye jaane chahiye.
 */
public class SimpleAuthService implements IAuthService {

    // Demo credentials — production mein hashed DB se compare karein
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    @Override
    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
