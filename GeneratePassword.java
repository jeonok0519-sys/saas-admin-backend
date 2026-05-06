import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("");
        System.out.println("Run this SQL to update the admin password:");
        System.out.println("UPDATE sys_operator SET password = '" + encodedPassword + "' WHERE username = 'admin';");
    }
}