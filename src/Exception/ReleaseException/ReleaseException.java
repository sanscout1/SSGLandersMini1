package Exception.ReleaseException;


public class ReleaseException extends RuntimeException{
  private ReleaseErrorCode errorCode;

  public ReleaseException(ReleaseErrorCode errorCode) {
    this.errorCode = errorCode;
    System.out.println("================================================================");
    System.out.println(this.errorCode.getMessage());
    System.out.println("================================================================");
  }
}
