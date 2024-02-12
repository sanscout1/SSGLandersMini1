package DAO;

import VO.StockVO;
import VO.WarehouseVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDao {

    private Connection conn = null;
    private static volatile StockDao instance;

    public static StockDao getInstance() {  //싱글톤
        if (instance == null) {
            instance = new StockDao();
        }
        return instance;
    }
    private StockDao(){
        createAdminStockRead();
    }

    private void connectDB() {
        try {
            //JDBC Driver 등록
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ssglandersretail?serverTimezone=Asia/Seoul",
                    "root",
                    "0000"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void closeDB() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    //회원용 read는 입고 - 출고로 재고 확인 가능하게 만들자.
    private void createUserStockRead(){
        try {
            connectDB();
            String sql = """
                    create procedure selectUserStock (out t_category varchar(20),out i_category varchar(20),out s_category varchar(20)
                    ,out pname varchar(30),out WID int, out wname varchar(15),out address_city varchar(10),out quantity int)
                    begin\s
                    select p.t_category, p.i_category, p.s_category, p.pname, w.WID, w.wname, w.address_city, r.p_quantity-r2.p_quantity\s
                        from receipt r join ssglandersretail.release r2 on r.UID = r2.UID\s
                        natural join product p join warehouse w on w.WID = s.WID;
                    end;\s""";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {}
        finally {
            closeDB();
        }
    }

    //총관리자용 read,
    private void createAdminStockRead(){
        try {
            connectDB();
            String sql = """
                    create procedure selectAdminStock (out t_category varchar(20),out i_category varchar(20),out s_category varchar(20)
                    ,out pname varchar(30),out WID int, out wname varchar(15),out address_city varchar(10),out quantity int)
                    begin\s
                    select p.t_category, p.i_category, p.s_category, p.pname, w.WID, w.wname, w.address_city, s.quantity\s
                        from stock s natural join product p join warehouse w on w.WID = s.WID;
                    end;\s""";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {}
        finally {
            closeDB();
        }
    }

    // 입고 출고 기능 구현 되면 테스팅 가능
    public List<StockVO> stockRead() {
        List<StockVO> StockVOList = null;
        try {
            connectDB();
            //if(this.myUser.getUtype ==1){}
            String sql = "{call selectAdminStock(?,?,?,?,?,?,?,?)}";
            //else if(this.myUser.getUtype ==2 ){ String sql = "{call selectUserStock(?,?,?,?,?,?,?,?)}"; }
            CallableStatement cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            StockVOList = new ArrayList<StockVO>();
            while (rs.next()){
                StockVO vo = new StockVO(rs.getString(1),rs.getString(2),rs.getString(3)
                        ,rs.getString(4),rs.getInt(5),rs.getString(6),rs.getString(7),
                        rs.getInt(8));
                StockVOList.add(vo);
            }
            rs.close();
            cstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
        return StockVOList;
    }
}
