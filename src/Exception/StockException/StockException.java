package Exception.StockException;

public class StockException extends RuntimeException {
    private StockErrorCode errorCode;

    public StockException(StockErrorCode errorCode) {
        this.errorCode = errorCode;
        System.out.println("================================================================");
        System.out.println(this.errorCode.getMessage());
        System.out.println("================================================================");
    }
}


