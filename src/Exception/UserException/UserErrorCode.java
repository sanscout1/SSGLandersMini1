package Exception.UserException;

public enum UserErrorCode {
    INVALID_ID("올바른 형식의 아이디가 아닙니다. 다시 시도해주세요.", "E01"),
    INVALID_PASSWORD("올바른 형식의 비밀번호가 아닙니다.", "E02"),
    INVALID_ID_LENGTH("아이디는 최소 6자 이상이어야 합니다.", "E03"),
    INVALID_PASSWORD_LENGTH("비밀번호는 최소 8자 이상이어야 합니다.", "E04"),
    INVALID_LOGIN("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해주세요.", "E05");

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

