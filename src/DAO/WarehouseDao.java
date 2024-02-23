package DAO;


import VO.WarehouseVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDao extends DBconnector{

    private static volatile WarehouseDao instance;

    public static WarehouseDao getInstance() {  //싱글톤
        if (instance == null) {
            instance = new WarehouseDao();
        }
        return instance;
    }


    public void warehouseCreate(WarehouseVO warehouse) {
        try {
            connectDB();
            String sql = "insert into warehouse " +
                    "(UID,wtype,wname,address_city,totalcapacity,usingcapacity,charge,cost)" +
                    "values(?,?,?,?,?,0,?,?) ";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, warehouse.getUserID());
            pstmt.setInt(2, warehouse.getWarehouseType());
            pstmt.setString(3, warehouse.getWarehouseName());
            pstmt.setString(4, warehouse.getAddressCity());
            pstmt.setInt(5, warehouse.getTotalCapacity());
            pstmt.setInt(6, warehouse.getCharge());
            pstmt.setInt(7, warehouse.getCost());
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("창고 정보가 저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }
    public List<WarehouseVO> warehouseRead() {
        List<WarehouseVO> warehouseVOList = null;
        try {
            connectDB();
            String sql = "select WID,UID,wtype,wname,address_city,totalcapacity,usingcapacity,charge,cost " +
                    "from warehouse";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            warehouseVOList = new ArrayList<WarehouseVO>();

            while (rs.next()){
                WarehouseVO vo = WarehouseVO.builder().warehouseID(rs.getInt("WID")).userID(rs.getInt("UID"))
                        .warehouseType(rs.getInt("wtype")).warehouseName(rs.getString("wname"))
                        .addressCity(rs.getString("address_city")).totalCapacity(rs.getInt("totalcapacity"))
                        .usingCapacity(rs.getInt("usingcapacity")).charge(rs.getInt("charge"))
                        .cost(rs.getInt("cost")).build();
//                WarehouseVO vo = new WarehouseVO(rs.getInt("WID"),rs.getInt("UID")
//                ,rs.getInt("wtype"),rs.getString("wname"),rs.getString("address_city")
//                ,rs.getInt("totalcapacity"),rs.getInt("usingcapacity"),rs.getInt("charge")
//                ,rs.getInt("cost"));
//                warehouseVOList.add(vo);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
        return warehouseVOList;
    }

    public List<WarehouseVO> warehouseChargeRead(int wtype) {
        List<WarehouseVO> warehouseVOList = null;
        try {
            connectDB();
            String sql = "select WID,UID,wtype,wname,address_city,totalcapacity,usingcapacity,charge,cost " +
                    "from warehouse " +
                    "where wtype = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, wtype);
            ResultSet rs = pstmt.executeQuery();
            warehouseVOList = new ArrayList<WarehouseVO>();

            while (rs.next()){
                WarehouseVO vo = new WarehouseVO(rs.getInt("WID"),rs.getInt("UID")
                        ,rs.getInt("wtype"),rs.getString("wname"),rs.getString("address_city")
                        ,rs.getInt("totalcapacity"),rs.getInt("usingcapacity"),rs.getInt("charge")
                        ,rs.getInt("cost"));
                warehouseVOList.add(vo);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
        return warehouseVOList;
    }

}
