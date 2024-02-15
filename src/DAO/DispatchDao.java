package DAO;

import VO.DispatchVO;
import VO.ReleaseVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispatchDao extends DBconnector {

  // 배차 입력
  public int dispatchInsert(DispatchVO dispatchVO) {
    int max = 0;
    try {
      connectDB();

      String sql = "INSERT INTO dispatch " +
                   "(veh_id,dis_date,approval)" +
                   "values(?,?,?)";

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, dispatchVO.getVehicle());
      pstmt.setTimestamp(2, new Timestamp(dispatchVO.getDate().getTime()));
      pstmt.setInt(3, 1);

      int rows = pstmt.executeUpdate();


      // 가장큰 d_id 가져오기
      sql = "select Did from dispatch order by Did DESC LIMIT 1";
      pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        max = rs.getInt("Did");
      }

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      System.out.println();
    }
    finally {
      closeDB();
    }
    return max;
  }

  // 배차 리스트 출력
  public List<DispatchVO> dispatchListSelect() {

    List<DispatchVO> dispatchVOList = new ArrayList<>();

    try {
      connectDB();
      String sql = new StringBuilder().append("SELECT * FROM dispatch").toString();
      PreparedStatement pstmt = conn.prepareStatement(sql);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        DispatchVO dispatchVO = new DispatchVO();
        dispatchVO.setId(rs.getInt("Did"));
        dispatchVO.setVehicle(rs.getInt("veh_id"));
        // new java.sql.Timestamp(rs.getDate("dis_date").getTime())
//        dispatchVO.setDate(new java.sql.Timestamp(rs.getDate("dis_date").getTime()));
        dispatchVO.setDate(rs.getDate("dis_date"));
        dispatchVO.setApproval(rs.getInt("approval"));
        dispatchVOList.add(dispatchVO);
      }
      rs.close();
      pstmt.close();
      closeDB();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return dispatchVOList;
  }

  // 배차 정보 수정
  public boolean dispatchUpdate(int searchNum, int vehicleNum) {
    try {
      connectDB();

      String sql = new StringBuilder().append("UPDATE dispatch SET ")
              .append("veh_id=? ")
              .append("where Did=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, vehicleNum);
      pstmt.setInt(2, searchNum);

      int rows = pstmt.executeUpdate();
      if(rows == 0){
        System.out.println("*해당하는 배차번호가 없습니다*");
        return false;
      }

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {

    }finally {
      closeDB();
    }
    return true;
  }

  // 선택한 번호 approval 0으로 변경
  public boolean dispatchDelete(int deleteNum, int approvalNum) {
    try {
      connectDB();
      String sql = new StringBuilder().append("UPDATE dispatch SET ")
              .append("approval=? ")
              .append("where Did=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, approvalNum);
      pstmt.setInt(2, deleteNum);

      int rows = pstmt.executeUpdate();
      if(rows == 0){
        System.out.println("*해당하는 배차번호가 없습니다*");
        return false;
      }

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      closeDB();
    }
    return true;
  }

  // 선택한 번호 배차 출력
  public void dispatchSelect(int selectNum) {

  }

}
