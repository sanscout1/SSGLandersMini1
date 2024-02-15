package Exception.ReleaseException;

public enum ReleaseErrorCode {
  INVALID_NUMBER("숫자가 아닙니다.", "E01"),
  INVALID_ZERO("1이상 숫자 입력해주세요", "E02"),
  INVALID_APRROVE("0또는 1 숫자만 입력해주세요", "E03"),
  INVALID_PASSWORD_LENGTH1("비밀번호는 최소 8자 이상이어야 합니다.1", "E04"),
  INVALID_LOGIN1("아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해주세요1.", "E05");

  private String message;
  private String code;

  ReleaseErrorCode(String message, String code) {
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
