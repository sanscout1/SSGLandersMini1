package Exception.UserException;


import Exception.FinanceException.FinanceErrorCode;
import Exception.FinanceException.FinanceException;
import Exception.UserException.UserException;

public class UserExceptionList {
    public static void validateInteger(String num) {
        String regex = "[\\d+]*";
        if (!num.matches(regex)) {
            throw new UserException(UserErrorCode.INVALID_NUM);
        }
    }

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

    public static void validateAddressNum(String input) {
        try {
            if (!input.isEmpty()) {
                if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 127) {
                    throw new UserException(UserErrorCode.INVALID_ADDRESS_NUM);
                }
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_ADDRESS_NUM);
        }
    }

    public static void validateMenuSelection(int input) {
        try {
            if (input < 1 || input > 4) {
                throw new UserException(UserErrorCode.INVALID_MENU_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_MENU_SELECTION);
        }
    }

    public static void validateMenusSelection(int input) {
        try {
            if (input < 1 || input > 5) {
                throw new UserException(UserErrorCode.INVALID_MENUS_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_MENUS_SELECTION);
        }
    }

    public static void validateNumberSelection(int input) {
        try {
            if (input < 1 || input > 2) {
                throw new UserException(UserErrorCode.INVALID_NUMBER_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_NUMBER_SELECTION);
        }
    }

    public static void validateNumbersSelection(int input) {
        try {
            if (input < 1 || input > 3) {
                throw new UserException(UserErrorCode.INVALID_NUMBERS_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_NUMBERS_SELECTION);
        }
    }

    public static void validateApproval(String input) {
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false") && !input.isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_APPROVAL);
        }
    }

    public static void failedToDeleteUser() {
        throw new UserException(UserErrorCode.FAILED_TO_DELETE_USER);
    }

    public static void validateUserType(String input) {
        try {
            if (!input.isEmpty()) {
                if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 2) {
                    throw new UserException(UserErrorCode.INVALID_NUMBER_SELECTION);
                }
            }
        } catch (NumberFormatException e) {
            throw new UserException(UserErrorCode.INVALID_NUMBER_SELECTION);
        }
    }
}
