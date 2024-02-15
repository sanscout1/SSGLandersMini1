package Exception.FinanceException;

public enum FinanceErrorCode {
    INVALID_NAME("올바른 형식의 명칭이 아닙니다. 다시 시도해주세요.", "E01"),
    INVALID_ADDRESS("올바른 형식의 주소가 아닙니다.", "E02"),
    INVALID_NUM("숫자를 입력해주세요.", "E03"),

    INVALID_MENU_SELECTION("1부터 4사이의 숫자를 입력해주세요.", "E06"),

    INVALID_NUMBER_SELECTION("1부터 2사이의 숫자를 입력해주세요.", "E08"),
    INVALID_NUMBERS_SELECTION("1부터 3사이의 숫자를 입력해주세요.", "E09");

    private String message;
    private String code;

    FinanceErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

}

