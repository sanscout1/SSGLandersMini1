package Exception.UserException;


public class UserExceptionList {

    public static void validateUserID(String ID) {
        String regex = "^[a-z0-9]+$";
        if (!ID.matches(regex)) {
            throw new UserException(UserErrorCode.INVALID_ID);
        }
        if (ID.length() < 6) {
            throw new UserException(UserErrorCode.INVALID_ID_LENGTH);
        }
    }

    public static void validatePassword(String password) {
        // Modify the regex according to your password requirements
        String regex = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]+$";
        if (!password.matches(regex)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        if (password.length() < 8) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD_LENGTH);
        }
    }

    public static void invalidLogin() {
        throw new UserException(UserErrorCode.INVALID_LOGIN);
    }
}
