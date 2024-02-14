package Exception.UserException;

public class UserException extends RuntimeException {
    private UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
        System.out.println("================================================================");
        System.out.println(this.errorCode.getMessage());
        System.out.println("================================================================");
    }
}


