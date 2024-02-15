package Exception.ReceiptException;

public enum ReceiptErrorCode {

    INVALID_CHOICE("올바른 형식의 선택이 아닙니다. 다시 시도해주세요","E01"),
    INVALID_INPUT_ERROR("올바른 형식의 입력이 아닙니다. 다시 시도해주세요.","E02");

    private String message;
    private String code;

    ReceiptErrorCode(String message, String code){
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
