package Exception.FinanceException;

public class FinanceException extends RuntimeException {
    private FinanceErrorCode errorCode;

    public FinanceException(FinanceErrorCode errorCode) {
        this.errorCode = errorCode;
        System.out.println("================================================================");
        System.out.println(this.errorCode.getMessage());
        System.out.println("================================================================");
    }
}


