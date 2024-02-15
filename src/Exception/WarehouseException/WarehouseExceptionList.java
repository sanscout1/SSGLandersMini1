package Exception.WarehouseException;


public class WarehouseExceptionList {

    public static void validateWarehouseName(String ID) {
        String regex = "^[가-힣a-zA-Z]+$";
        if (!ID.matches(regex)) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_NAME);
        }
    }

    public static void validateAddress(String address) {
        String regex = "^[가-힣a-zA-Z]+$";
        if (!address.matches(regex)) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_ADDRESS);
        }
    }
    public static void validateInteger(String num) {
        String regex = "[\\d+]*";
        if (!num.matches(regex)) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_NUM);
        }
    }

    public static void validateMenuSelection(int input) {
        try {
            if (input < 1 || input > 4) {
                throw new WarehouseException(WarehouseErrorCode.INVALID_MENU_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_MENU_SELECTION);
        }
    }

    public static void validateNumberSelection(int input) {
        try {
            if (input < 1 || input > 2) {
                throw new WarehouseException(WarehouseErrorCode.INVALID_NUMBER_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_NUMBER_SELECTION);
        }
    }
    public static void validateNumbersSelection(int input) {
        try {
            if (input < 1 || input > 3) {
                throw new WarehouseException(WarehouseErrorCode.INVALID_NUMBERS_SELECTION);
            }
        } catch (NumberFormatException e) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_NUMBERS_SELECTION);
        }
    }


}
