package DAO;

import VO.ReleaseVO;
import VO.WaybillVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaybillDao {
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

  // 운송장 내역 출력
  public List<WaybillVO> waybillListSelect(){
    List<WaybillVO> waybillVOList = new ArrayList<>();

    try {
//      Class.forName("com.mysql.cj.jdbc.Driver");
//      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssglandersretail?serverTimezone=Asia/Seoul", "root", "1111");

      String sql = new StringBuilder().append("SELECT * FROM waybill").toString();
      PreparedStatement pstmt = conn.prepareStatement(sql);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        WaybillVO waybillVO = new WaybillVO();
        waybillVO.setId(rs.getInt("way_id"));
        waybillVO.setDep_name(rs.getString("dep_name"));
        waybillVO.setDep_city(rs.getString("dep_city"));
        waybillVO.setDep_city_num(rs.getInt("dep_city_num"));
        waybillVO.setArr_city(rs.getString("arr_city_num"));
        waybillVO.setArr_name(rs.getString("arr_name"));
        waybillVO.setArr_city_num(rs.getInt("arr_city_num"));
        waybillVO.setWay_num(rs.getInt("way_num"));


        waybillVOList.add(waybillVO);

      }
//
      rs.close();
      pstmt.close();


    } catch (Exception e) {
      e.printStackTrace();
    }
    return waybillVOList;
  }

  // 운송장 추가
  public void waybillInsert(WaybillVO waybillVO){
    try {
      String sql = "INSERT INTO waybill " +
              "(dep_name,dep_city,dep_city_num,arr_city, arr_city_num,arr_name,way_num)" +
              "values(?,?,?,?,?,?,?)";

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, waybillVO.getDep_name());
      pstmt.setString(2, waybillVO.getDep_city());
      pstmt.setInt(3, waybillVO.getDep_city_num());
      pstmt.setString(4, waybillVO.getArr_city());
      pstmt.setInt(5, waybillVO.getArr_city_num());
      pstmt.setString(6, waybillVO.getArr_name());
      pstmt.setInt(7, waybillVO.getWay_num());


      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      //PreparedStatement 닫기
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // 운송장 수정
  public void waybillUpdate(int searchNum, WaybillVO waybillVO){
    try {

      String sql = new StringBuilder().append("UPDATE waybill SET ")
              .append("dep_name=? ,")
              .append("dep_city=? ,")
              .append("dep_city_num=? ,")
              .append("arr_name=? ,")
              .append("arr_city=? ,")
              .append("arr_city_num=? ,")
              .append("way_num =? ")
              .append("where way_id=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, waybillVO.getDep_name());
      pstmt.setString(2, waybillVO.getDep_city());
      pstmt.setInt(3, waybillVO.getDep_city_num());
      pstmt.setString(4, waybillVO.getArr_name());
      pstmt.setString(5, waybillVO.getArr_city());
      pstmt.setInt(6, waybillVO.getArr_city_num());
      pstmt.setInt(7, waybillVO.getWay_num());
      pstmt.setInt(8, searchNum);

      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      //PreparedStatement 닫기
      pstmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 운송장 한개 출력??
  public WaybillVO waybillSelect(int selectNum){
    return new WaybillVO();
  }
}
