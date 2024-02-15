package DAO;

import VO.ReleaseVO;
import VO.UserVO;
import VO.WaybillVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaybillDao extends DBconnector{


  // 운송장 내역 출력
  public List<WaybillVO> waybillListSelect(UserVO userVO){
    List<WaybillVO> waybillVOList = new ArrayList<>();
    PreparedStatement pstmt;
    try {
      connectDB();

      if(userVO.getUserType() == 1){
        String sql = new StringBuilder().append("SELECT * FROM waybill").toString();
        pstmt = conn.prepareStatement(sql);
      }else {
        String sql = new StringBuilder().append("SELECT * FROM ssglandersretail.waybill w join ssglandersretail.release r on w.way_id = r.WID WHERE r.UID = ?").toString();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userVO.getUserID());
      }

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
    }finally {
      closeDB();
    }
    return waybillVOList;
  }

  // 운송장 추가
  public int waybillInsert(WaybillVO waybillVO){
    int max = 0;
    try {
      connectDB();
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


      // 가장큰 way_id 가져오기
      System.out.println("이부분은 왔나?");
      sql = "select way_id from waybill order by way_id DESC LIMIT 1";
      pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();

      if(rs.next()){
        max = rs.getInt("way_id");
      }

      //PreparedStatement 닫기
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closeDB();
    }
    return max;
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
