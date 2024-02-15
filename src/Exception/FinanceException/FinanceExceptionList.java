package Exception.FinanceException;


public class FinanceExceptionList {

    public static void validateInteger(String num) {
        String regex = "[\\d+]*";
        if (!num.matches(regex)) {
            throw new FinanceException(FinanceErrorCode.INVALID_NUM);
        }
    }

    public static void validateNumberSelection(int input) {
        try {
            if (input < 1 || input > 2) {
                throw new FinanceException(FinanceErrorCode.INVALID_NUMBER_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new FinanceException(FinanceErrorCode.INVALID_NUMBER_SELECTION);
        }
    }
    public static void validateNumbersSelection(int input) {
        try {
            if (input < 1 || input > 3) {
                throw new FinanceException(FinanceErrorCode.INVALID_NUMBERS_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new FinanceException(FinanceErrorCode.INVALID_NUMBERS_SELECTION);
        }
    }


}
