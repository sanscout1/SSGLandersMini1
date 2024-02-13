package DAO;

import VO.FinanceVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceDao {
    private Connection conn = null;
    private static volatile FinanceDao instance;

    public static FinanceDao getInstance() {  //싱글톤
        if (instance == null) {
            instance = new FinanceDao();
        }
        return instance;
    }
    private FinanceDao(){
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

    // 모든 창고 단위 조회
    public  List<FinanceVO> financeTypeRead(int identifier) {
        List<FinanceVO> financeVOList = null;
        try {
            connectDB();
            StringBuilder sql = new StringBuilder();
            sql.append("select rec_id,ftype,fdate,amount ").append("from finance ");
            if(identifier == 2){
                sql.append("where ftype = 0");
            } else if(identifier == 3){
                sql.append("where ftype = 1");
            }
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            financeVOList = new ArrayList<FinanceVO>();

            while (rs.next()){
                FinanceVO vo = new FinanceVO(rs.getInt("rec_id"),rs.getInt("ftype")
                        ,rs.getString("fdate"),rs.getInt("amount"));
                financeVOList.add(vo);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
        return financeVOList;
    }
    public  List<FinanceVO> financeWarehouseRead(int identifier) {
        List<FinanceVO> financeVOList = null;
        try {
            connectDB();
            StringBuilder sql = new StringBuilder();
            sql.append("select WID,rec_id,ftype,fdate,amount ").append("from finance natural join receipt ");
            sql.append("where WID = ").append(identifier);

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            financeVOList = new ArrayList<FinanceVO>();

            while (rs.next()){
                FinanceVO vo = new FinanceVO(rs.getInt("WID"),rs.getInt("rec_id"),rs.getInt("ftype")
                        ,rs.getString("fdate"),rs.getInt("amount"));
                financeVOList.add(vo);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
        return financeVOList;
    }

    public void financeUpdate(int amount,int recid,int ftype){
        try {
            connectDB();
            String sql = "update finance set amount =? where rec_id =? and ftype =?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,amount);
            pstmt.setInt(2,recid);
            pstmt.setInt(3,ftype);
            pstmt.executeUpdate();
            System.out.println("업데이트 완료");
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
    }
    public void financeDelete(int recid,int ftype){
        try {
            connectDB();
            String sql = "delete from finance where rec_id =? and ftype = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,recid);
            pstmt.setInt(2,ftype);
            pstmt.executeUpdate();
            System.out.println("삭제 완료");
            pstmt.close();
        } catch (Exception e) {
        } finally {
            closeDB();
        }
    }

}
