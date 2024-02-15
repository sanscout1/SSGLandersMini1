package Exception.UserException;

public enum UserErrorCode {
    INVALID_ID("올바른 형식의 아이디가 아닙니다. 다시 시도해주세요.", "E01"),
    INVALID_PASSWORD("올바른 형식의 비밀번호가 아닙니다.", "E02"),
    INVALID_ID_LENGTH("아이디는 최소 6자 이상이어야 합니다.", "E03"),
    INVALID_PASSWORD_LENGTH("비밀번호는 최소 8자 이상이어야 합니다.", "E04"),
    INVALID_LOGIN("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해주세요.", "E05"),
    INVALID_MENU_SELECTION("1부터 4사이의 숫자를 입력해주세요.", "E06"),
    INVALID_MENUS_SELECTION("1부터 5사이의 숫자를 입력해주세요.", "E07"),
    INVALID_NUMBER_SELECTION("1부터 2사이의 숫자를 입력해주세요.", "E08"),
    INVALID_NUMBERS_SELECTION("1부터 3사이의 숫자를 입력해주세요.", "E09"),
    INVALID_APPROVAL("승인 여부를 'true' 또는 'false'로 입력해주세요.", "E10"),
    FAILED_TO_DELETE_USER("회원님의 재고가 남아있습니다.","E11"),
    INVALID_NUM("숫자를 입력해주세요.", "E12"),
    INVALID_ADDRESS_NUM("번지를 128 보다 작게 입력해주세요.", "E13");

    private String message;
    private String code;

    UserErrorCode(String message, String code) {
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

