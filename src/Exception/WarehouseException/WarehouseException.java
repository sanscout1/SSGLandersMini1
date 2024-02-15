package Exception.WarehouseException;

public class WarehouseException extends RuntimeException {
    private WarehouseErrorCode errorCode;

    public WarehouseException(WarehouseErrorCode errorCode) {
        this.errorCode = errorCode;
        System.out.println("================================================================");
        System.out.println(this.errorCode.getMessage());
        System.out.println("================================================================");
    }
}


