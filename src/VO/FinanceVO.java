package VO;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class FinanceVO {
    private int WarehouseID;
    private int recID;
    private int ftype;
    private String fdate;
    private int amount;

    public FinanceVO(int recID, int ftype, String fdate, int amount) {
        this.recID = recID;
        this.ftype = ftype;
        this.fdate = fdate;
        this.amount = amount;
    }

    public FinanceVO(int warehouseID, int recID, int ftype, String fdate, int amount) {
        WarehouseID = warehouseID;
        this.recID = recID;
        this.ftype = ftype;
        this.fdate = fdate;
        this.amount = amount;
    }
}
