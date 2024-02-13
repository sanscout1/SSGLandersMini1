package DAO;

import VO.DispatchVO;
import VO.ReleaseVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispatchDao {

  public Connection conn = null;

  public void connectDB() {
    try {
      System.out.println("실행됨");
      //JDBC Driver 등록
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssglandersretail?serverTimezone=Asia/Seoul", "root", "1111");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void closeDB() {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
    }
  }

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
      pstmt.setTimestamp(2, new java.sql.Timestamp(dispatchVO.getDate().getTime()));
      pstmt.setInt(3, 0);


      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      // 가장큰 d_id 가져오기
      System.out.println("이부분은 왔나?");
      sql = "select Did from dispatch order by Did DESC LIMIT 1";
      pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        max = rs.getInt("Did");
      }
      System.out.println(max + "***********");

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      e.printStackTrace();
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
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssglandersretail?serverTimezone=Asia/Seoul", "root", "1111");

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

    } catch (Exception e) {
      e.printStackTrace();
    }
    return dispatchVOList;
  }

  // 배차 정보 수정
  public void dispatchUpdate(int searchNum, int vehicleNum) {
    try {
      String sql = new StringBuilder().append("UPDATE dispatch SET ")
              .append("veh_id=? ")
              .append("where Did=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, vehicleNum);
      pstmt.setInt(2, searchNum);

      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 선택한 번호 approval 0으로 변경
  public void dispatchDelete(int deleteNum, int approvalNum) {
    try {
      String sql = new StringBuilder().append("UPDATE dispatch SET ")
              .append("approval=? ")
              .append("where Did=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, approvalNum);
      pstmt.setInt(2, deleteNum);

      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 선택한 번호 배차 출력
  public void dispatchSelect(int selectNum) {

  }

}
