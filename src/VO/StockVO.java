package VO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StockVO {
    private String tcategory;
    private String icategory;
    private String scategory;
    private String pname;
    private int warehouseID;
    private String warehouseName;
    private String addressCity;
    private int quantity;

    public StockVO(String tcategory, String icategory, String scategory, String pname, int warehouseID, String warehouseName, String addressCity, int quantity) {
        this.tcategory = tcategory;
        this.icategory = icategory;
        this.scategory = scategory;
        this.pname = pname;
        this.warehouseID = warehouseID;
        this.warehouseName = warehouseName;
        this.addressCity = addressCity;
        this.quantity = quantity;
    }
}
