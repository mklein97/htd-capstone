package learn.noodemy.domain;

import java.util.regex.Pattern;

public class Validations {
    final static Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean validEmail(String email) {
        return !isNullOrBlank(email)
                && EMAIL_PATTERN.matcher(email).find();
    }
}
