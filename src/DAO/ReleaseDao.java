package DAO;

import VO.ReceiptVO;
import VO.ReleaseVO;
import VO.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReleaseDao {
  private Connection conn = null;

  private void connectDB() {
    try {
      //JDBC Driver 등록
      Class.forName("com.mysql.cj.jdbc.Driver");

      conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ssglandersretail?serverTimezone=Asia/Seoul",
              "root",
              "1111"
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

  public ReleaseDao() {
//    createTrigger();
//    createTriggerWarehouse();
  }

  //회원용 read는 입고 - 출고로 재고 확인 가능하게 만들자.
  private void createTriggerStock() {
    // 승인시 stock의 quentity 줄이기
    try {
      connectDB();
      String sql = """
              CREATE PROCEDURE your_stock_trigger_procedure(IN release_id INT)
              BEGIN
                  DECLARE stock_quantity_diff INT;
                            
                  -- 출고 승인이 되었을 때만 실행
                  IF (SELECT approval FROM ssglandersretail.release WHERE Rel_ID = release_id) = 1 THEN
                      -- 변경 전과 변경 후의 출고 수량 차이 계산
                      SET stock_quantity_diff = (
                          SELECT s.quantity - r.p_quantity
                          FROM ssglandersretail.stock s
                          JOIN ssglandersretail.release r ON s.PID = r.PID AND s.WID = r.WID
                          WHERE r.Rel_ID = release_id
                      );
                            
                      -- stock 테이블의 재고 수량 업데이트
                      UPDATE ssglandersretail.stock
                      SET quantity = stock_quantity_diff
                      WHERE PID IN (SELECT PID FROM ssglandersretail.release WHERE Rel_ID = release_id)\s
                      AND WID IN (SELECT WID FROM ssglandersretail.release WHERE Rel_ID = release_id);
                  END IF;
              END;""";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
  }

  private void createTriggerWarehouse(){
    // 승인시 warehouse의 usingcapacity 양 줄이기
    try {
      connectDB();
      String sql = """
              CREATE PROCEDURE update_usingcapacity_after_approval(IN release_id INT)
              BEGIN
                  DECLARE product_capacity INT;
                  DECLARE released_quantity INT;
                            
                  -- 출고 승인일 때만 실행
                  IF (SELECT approval FROM `release` WHERE Rel_ID = release_id) = 1 THEN
                      -- 출고 테이블에서 출고된 수량 가져오기
                      SELECT p_quantity INTO released_quantity FROM `release` WHERE Rel_ID = release_id;
                            
                      -- 출고된 상품의 용량 가져오기
                      SELECT usecapacity INTO product_capacity
                      FROM product
                      WHERE PID = (SELECT PID FROM `release` WHERE Rel_ID = release_id);
                            
                      -- Warehouse 테이블의 usingcapacity 업데이트
                      UPDATE warehouse w
                      SET usingcapacity = usingcapacity - (released_quantity * product_capacity)
                      WHERE WID = (SELECT WID FROM `release` WHERE Rel_ID = release_id);
                  END IF;
              END;""";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
  }

  //출고리스트 출력
  public List<ReleaseVO> releaseListSelect(UserVO userVO) {

    List<ReleaseVO> releaseVOList = new ArrayList<>();
    PreparedStatement pstmt;

    try {
      connectDB();
      if (userVO.getUserType() == 1) {  // 관리자일 때
        String sql = new StringBuilder().append("SELECT * FROM ssglandersretail.release").toString();
        pstmt = conn.prepareStatement(sql);
      } else {  // 일반회원일때
        String sql = new StringBuilder().append("SELECT * FROM ssglandersretail.release WHERE UID = ?").toString();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userVO.getUserID());

      }

      //SQL문 실행
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        ReleaseVO releaseVO = new ReleaseVO();
        releaseVO.setId(rs.getInt("Rel_ID"));
        releaseVO.setDate(rs.getDate("rel_date"));
        releaseVO.setQuentity(rs.getInt("p_quantity"));
        releaseVO.setState(rs.getInt("state"));
        releaseVO.setApproval(rs.getInt("approval"));
        releaseVO.setDispatchId(rs.getInt("Did")); // 다른테이블
        releaseVO.setWaybillId(rs.getInt("way_id"));
        releaseVO.setUserId(rs.getInt("UID"));
        releaseVO.setWarehouseId(rs.getInt("WID"));
        releaseVO.setProductId(rs.getInt("PID"));

        releaseVOList.add(releaseVO);
      }

      rs.close();
      pstmt.close();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
    return releaseVOList;
  }

  public void releaseDispatchWaybillUpdate(int waybillNum, int dispatchNum) {

    int max = 0;
    try {
      connectDB();
      // 가장큰 Rel_id 가져오기
      String sql = "select Rel_id from ssglandersretail.release order by Rel_id DESC LIMIT 1";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        max = rs.getInt("Rel_id");
      }


      // 운송장번호, 배차번호
      sql = new StringBuilder().append("UPDATE ssglandersretail.release SET ")
              .append("way_id=? ,")
              .append("Did=? ")
              .append("where Rel_ID = ?")
              .toString();

      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, waybillNum);
      pstmt.setInt(2, dispatchNum);
      pstmt.setInt(3, max);

      int rows = pstmt.executeUpdate();
      System.out.println("저장된 행 수: " + rows);

      //PreparedStatement 닫기
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
  }

  // 요청 삽입, approval, state = 0 // dispatchid, waybillid = 000
  public void releaseRequestInsert(ReleaseVO release) {

    try {
      connectDB();
      String sql = "INSERT INTO ssglandersretail.release " +
                   "(rel_date,p_quantity,state,approval,Did,way_id,UID,WID,PID)" +
                   "values(?,?,?,?,?,?,?,?,?)";

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setTimestamp(1, new java.sql.Timestamp(release.getDate().getTime()));
      pstmt.setInt(2, release.getQuentity());
      pstmt.setInt(3, release.getState()); // state
      pstmt.setInt(4, release.getApproval()); // approval
      pstmt.setInt(5, release.getDispatchId()); // dispatch
      pstmt.setInt(6, release.getWaybillId()); // way_id
      pstmt.setInt(7, release.getUserId()); //
      pstmt.setInt(8, release.getWarehouseId()); //
      pstmt.setInt(9, release.getProductId()); //

      int rows = pstmt.executeUpdate();

      //PreparedStatement 닫기
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
  }

  // 상품 입력 후 해당 리스트 출력
  public List<ReleaseVO> releaseSearchSelect(UserVO userVO, String product) {
    List<ReleaseVO> releaseVOList = new ArrayList<>();
    PreparedStatement pstmt;
    try {
      connectDB();

      if (userVO.getUserType() == 1) {
        String sql = new StringBuilder().append("select * from ssglandersretail.release r join ssglandersretail.product p on r.pid = p.pid WHERE p.pname like ?").toString();
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + product + "%");
      } else {
        String sql = new StringBuilder().append("select * from ssglandersretail.release r join ssglandersretail.product p on r.pid = p.pid WHERE p.pname like ? AND r.UID = ?").toString();
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + product + "%");
        pstmt.setString(2, userVO.getID());
      }

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {

        ReleaseVO releaseVO = new ReleaseVO();
        releaseVO.setId(rs.getInt("Rel_ID"));
        releaseVO.setDate(rs.getDate("rel_date"));
        releaseVO.setQuentity(rs.getInt("p_quantity"));
        releaseVO.setState(rs.getInt("state"));
        releaseVO.setApproval(rs.getInt("approval"));
        releaseVO.setDispatchId(rs.getInt("Did"));
        releaseVO.setWaybillId(rs.getInt("way_id"));
        releaseVO.setUserId(rs.getInt("UID"));
        releaseVO.setWarehouseId(rs.getInt("WID"));
        releaseVO.setProductId(rs.getInt("PID"));

        releaseVOList.add(releaseVO);

      }
      for (ReleaseVO releaseVO : releaseVOList) System.out.println(releaseVO.toString());

      rs.close();
      pstmt.close();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }

    return releaseVOList;
  }

  // 미승인리스트 출력
  public List<ReleaseVO> releaseApproveListSelect() {
    List<ReleaseVO> releaseVOList = new ArrayList<>();

    try {
      connectDB();
      String sql = new StringBuilder().append("select * from ssglandersretail.release where approval = 0").toString();

      //PreparedStatement 얻기 및 값 지정
      PreparedStatement pstmt = conn.prepareStatement(sql);
      // PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

      //SQL문 실행
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {

        ReleaseVO releaseVO = new ReleaseVO();
        releaseVO.setId(rs.getInt("Rel_ID"));
        releaseVO.setDate(rs.getDate("rel_date"));
        releaseVO.setQuentity(rs.getInt("p_quantity"));
        releaseVO.setState(rs.getInt("state"));
        releaseVO.setApproval(rs.getInt("approval"));
        releaseVO.setDispatchId(rs.getInt("Did")); // 다른테이블
        releaseVO.setWaybillId(rs.getInt("way_id"));
        releaseVO.setUserId(rs.getInt("UID"));
        releaseVO.setWarehouseId(rs.getInt("WID"));
        releaseVO.setProductId(rs.getInt("PID"));

        releaseVOList.add(releaseVO);

      }
      for (ReleaseVO releaseVO : releaseVOList) System.out.println(releaseVO.toString());

      rs.close();
      pstmt.close();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }

    return releaseVOList;

  }

  // 승인내용 수정
  public void releaseApproveUpdate(int searchNum, int approvalNum) {

    try {
      connectDB();
      String sql = new StringBuilder().append("UPDATE ssglandersretail.release SET ")
              .append("approval=? ")
              .append("where Rel_ID=?")
              .toString();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, approvalNum);
      pstmt.setInt(2, searchNum);

      int rows = pstmt.executeUpdate();

      // 출고에서 수량줄이기 프로시저
      CallableStatement cstmt = conn.prepareCall("{call your_stock_trigger_procedure(?)}");
      // 프로시저 매개변수 설정
      cstmt.setInt(1, searchNum);
      // 프로시저 호출
      cstmt.execute();

      // 창고에서 usingcapacity 줄이기 프로시저
      cstmt = conn.prepareCall("{call update_usingcapacity_after_approval(?)}");
      cstmt.setInt(1, searchNum);
      cstmt.execute();


      cstmt.close();
      pstmt.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeDB();
    }
  }

  // 출고 id로 출고 물품 출력
  public void releaseSelect(int id) {

  }


}
