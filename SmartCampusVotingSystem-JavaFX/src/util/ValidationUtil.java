package util;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && studentId.matches("^[A-Za-z0-9]{4,20}$");
    }

    public static boolean isValidPassword(String password) {
        // At least 8 chars, one digit, one letter
        return password != null && password.length() >= 8
            && password.matches(".*[a-zA-Z].*")
            && password.matches(".*\\d.*");
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
