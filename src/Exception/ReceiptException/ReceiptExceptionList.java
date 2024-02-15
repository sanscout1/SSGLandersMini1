package Exception.ReceiptException;

public class ReceiptExceptionList {
    public static void validateAdminChoice(String choice) {
        String regex = "[1-6]";
        if (!choice.matches(regex)) {
            throw new ReceiptException(ReceiptErrorCode.INVALID_CHOICE);
        }
        if (choice.length() > 1){
            throw new ReceiptException(ReceiptErrorCode.INVALID_CHOICE);
        }
    }

    public static void validateMenuChoice(String choice) {
        String regex = "[1-5]";
        if (!choice.matches(regex)) {
            throw new ReceiptException(ReceiptErrorCode.INVALID_CHOICE);
        }
        if (choice.length() > 1){
            throw new ReceiptException(ReceiptErrorCode.INVALID_CHOICE);
        }
    }

    public static void validateInput(String input){
        String regex = "[0-9]+";
        if (!input.matches(regex)){
            throw new ReceiptException(ReceiptErrorCode.INVALID_INPUT_ERROR);
        }
    }

    public static void validateWarehouseId(String choice){
        String regex = "[0-9]+";
        if (!choice.matches(regex)){
            throw new ReceiptException(ReceiptErrorCode.INVALID_INPUT_ERROR);
        }
    }
    public static void validateInputChoice(String input){
        String regex = "[yn]";
        if (!input.matches(regex)){
            throw new ReceiptException(ReceiptErrorCode.INVALID_INPUT_ERROR);
        }
        if (input.length() > 1){
            throw new ReceiptException(ReceiptErrorCode.INVALID_CHOICE);
        }
    }


}
