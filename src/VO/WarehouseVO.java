package VO;

import DAO.WarehouseDao;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WarehouseVO {

    private int warehouseID;
    private int userID;
    private int warehouseType;
    private String warehouseName;
    private String addressCity;
    private int totalCapacity;
    private int usingCapacity;
    private int charge;  // 사용자에게 부가할 용량당 가격
    private int cost;   // 용량당 창고 유지비용

    public WarehouseVO(int uid, int wtype, String wname, String city, int totalCapacity, int charge, int cost) {
        this.userID = uid;
        this.warehouseType =wtype;
        this.warehouseName=wname;
        this.addressCity=city;
        this.totalCapacity=totalCapacity;
        this.charge=charge;
        this.cost=cost;
    }
    public WarehouseVO(int warehouseID, int userID, int warehouseType, String warehouseName, String addressCity, int totalCapacity, int usingCapacity, int charge, int cost) {
        this(userID,warehouseType,warehouseName,addressCity,totalCapacity,charge,cost);
        this.warehouseID=warehouseID;
        this.usingCapacity=usingCapacity;
    }


}
