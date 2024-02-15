package Exception.ReleaseException;


public class ReleaseExceptionList {

  // 1이상 숫자만 입력
  public static void validOverZeroNumber(String inputNum) {
    String regex = "[1-9]\\\\d*|\\\\d{2,}";
    if (!inputNum.matches(regex)) {
      throw new ReleaseException(ReleaseErrorCode.INVALID_NUMBER);
    }
    if (inputNum.equals("0")) {
      throw new ReleaseException(ReleaseErrorCode.INVALID_ZERO);
    }
  }

  public static void validZeoroOneNumber(String inputNum) {
    // Modify the regex according to your password requirements
    String regex = "^[0-1]+$";
    if (!inputNum.matches(regex)) {
      throw new ReleaseException(ReleaseErrorCode.INVALID_APRROVE);
    }
  }

}
