package main;

/**
 * ISP + DIP (Interface Segregation & Dependency Inversion Principle):
 * Main class sirf is ek-method interface par depend karta hai — concrete
 * authentication mechanism se decoupled. Kal LDAP ya JWT-based auth
 * banana ho to sirf naya IAuthService implement karo.
 */
public interface IAuthService {
    boolean authenticate(String username, String password);
}
