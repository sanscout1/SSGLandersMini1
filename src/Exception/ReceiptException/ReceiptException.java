package Exception.ReceiptException;

public class ReceiptException extends RuntimeException{
    private ReceiptErrorCode errorCode;
    public ReceiptException(ReceiptErrorCode errorCode){
        this.errorCode = errorCode;
        System.out.println("================================================================");
        System.out.println(this.errorCode.getMessage());
        System.out.println("================================================================");
    }
}
