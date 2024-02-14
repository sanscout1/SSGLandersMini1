package DAO;

import VO.FinanceVO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FinanceDao extends DBconnector {

    private static volatile FinanceDao instance;

    public static FinanceDao getInstance() {  //싱글톤
        if (instance == null) {
            instance = new FinanceDao();
        }
        return instance;
    }
    private FinanceDao(){
//        createTriggerChargeFinance();
//        createTriggerCostFinance();
    }



    private void createTriggerChargeFinance(){
        try {
            connectDB();
            String sql = "CREATE TRIGGER approvalcharge AFTER UPDATE ON receipt\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    IF NEW.approval = 1 AND OLD.approval = 0 THEN\n" +
                    "        INSERT INTO finance (fdate, amount, rec_id, ftype)\n" +
                    "        VALUES (NEW.rec_date, NEW.p_quantity * (SELECT w.charge FROM warehouse w WHERE w.WID = NEW.WID), NEW.Rec_ID, 0);\n" +
                    "    END IF;\n" +
                    "END;\n";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {e.printStackTrace();}
        finally {
            closeDB();
        }
    }
    private void createTriggerCostFinance(){
        try {
            connectDB();
            String sql = "CREATE TRIGGER approvalcost AFTER UPDATE ON receipt\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    IF NEW.approval = 1 AND OLD.approval = 0 THEN\n" +
                    "        INSERT INTO finance (fdate, amount, rec_id, ftype)\n" +
                    "        VALUES (NEW.rec_date, NEW.p_quantity * (SELECT w.cost FROM warehouse w WHERE w.WID = NEW.WID), NEW.Rec_ID, 1);\n" +
                    "    END IF;\n" +
                    "END;\n";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {e.printStackTrace();}
        finally {
            closeDB();
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
