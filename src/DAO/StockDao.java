package DAO;

import VO.StockVO;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDao extends DBconnector{

    private static volatile StockDao instance;

    public static StockDao getInstance() {  //싱글톤
        if (instance == null) {
            instance = new StockDao();
        }
        return instance;
    }
    private StockDao(){
 //       createAdminStockRead();
 //       createUserStockRead();
    }


    //회원용 read는 입고 - 출고로 재고 확인 가능하게 만들자.
    private void createUserStockRead(){
        try {
            connectDB();
            String sql = """
                    CREATE PROCEDURE selectUserStock (
                        IN in_UID INT
                    )
                    BEGIN
                        DECLARE done INT DEFAULT FALSE;
                        DECLARE cur_pid, cur_wid INT;
                        DECLARE total_receipt_quantity, total_release_quantity, stock_quantity INT;
                        DECLARE cur_product CURSOR FOR
                            SELECT DISTINCT PID, WID FROM receipt WHERE UID = in_UID;
                        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
                                        
                        -- Create temporary table to store stock information
                        CREATE TEMPORARY TABLE IF NOT EXISTS tmp_stock (
                            PID INT,
                            WID INT,
                            quantity INT
                        );
                                        
                        OPEN cur_product;
                                        
                        read_loop: LOOP
                            FETCH cur_product INTO cur_pid, cur_wid;
                            IF done THEN
                                LEAVE read_loop;
                            END IF;
                                        
                            -- Calculate total receipt quantity for the current product
                            SELECT COALESCE(SUM(p_quantity), 0) INTO total_receipt_quantity
                            FROM receipt
                            WHERE UID = in_UID AND PID = cur_pid AND WID = cur_wid;
                                        
                            -- Calculate total release quantity for the current product
                            SELECT COALESCE(SUM(p_quantity), 0) INTO total_release_quantity
                            FROM `release`
                            WHERE UID = in_UID AND PID = cur_pid AND WID = cur_wid;
                                        
                            -- Calculate stock quantity
                            SET stock_quantity = total_receipt_quantity - total_release_quantity;
                                        
                            -- Insert non-zero stock quantity into temporary table
                            IF stock_quantity > 0 THEN
                                INSERT INTO tmp_stock (PID, WID, quantity)
                                VALUES (cur_pid, cur_wid, stock_quantity);
                            END IF;
                        END LOOP;
                                        
                        CLOSE cur_product;
                                        
                        -- Select the stock information from the temporary table
                        SELECT\s
                            p.t_category,\s
                            p.i_category,\s
                            p.s_category,\s
                            p.pname,\s
                            s.WID,\s
                            w.wname,\s
                            w.address_city,\s
                            s.quantity
                        FROM\s
                            tmp_stock s
                        JOIN\s
                            product p ON s.PID = p.PID
                        JOIN\s
                            warehouse w ON s.WID = w.WID;
                                        
                        -- Drop temporary table
                        DROP TEMPORARY TABLE IF EXISTS tmp_stock;
                    END ;""";
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
    public List<StockVO> stockAdminRead() {
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
    public List<StockVO> stockUserRead(int userID) {
        List<StockVO> StockVOList = null;
        try {
            connectDB();
            String sql = "{call selectUserStock(?)}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setInt(1,userID);
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
