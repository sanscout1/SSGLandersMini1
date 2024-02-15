package DAO;


import VO.ReceiptVO;
import VO.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDao extends DBconnector{

    List<ReceiptVO> receiptVOList = new ArrayList<>();

    public ReceiptDao() {
  //      receiptStockTrigger();
   //     receiptWarehouseTrigger();
    }


    public void receiptStockTrigger() {     //  입고 시 재고에 있는 수량 증가 트리거
        try {
            connectDB();
            String sql =
                    "CREATE TRIGGER update_stock_quantity_trigger " +
                            "AFTER UPDATE ON receipt " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "IF NEW.approval = 1 AND OLD.approval != 1 THEN " +
//                    -- stock 테이블의 quantity 업데이트
                            "UPDATE stock " +
                            "SET quantity = quantity + NEW.p_quantity " +
                            "WHERE PID = NEW.PID; " +
                            "END IF; " +
                            "END";

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    // approval 이 1로 업데이트 되는 시점에
    // 조건 usingCapacity 는 totalCapacity 보다 클 수 없음
    // 근데 useCapacity 라는 물건의 무게가 있음 그래서 productQuantity 를 곱해서 더해줘야함
    // warehouse 의 old.usingCapacity = new.usingCapacity + (r.productQuantity * w.useCapacity)
    public void receiptWarehouseTrigger() {
        try {
            connectDB();
            String sql =
                    "CREATE TRIGGER update_using_capacity_trigger " +
                            "AFTER UPDATE ON receipt " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "DECLARE product_use_capacity DECIMAL(10, 2); " +
                            "IF NEW.approval = 1 AND OLD.approval != 1 THEN " +
//                    -- product 테이블의 useCapacity 가져오기
                            "SET product_use_capacity = (SELECT useCapacity FROM product WHERE PID = NEW.PID); " +
//            -- warehouse 테이블의 usingCapacity 업데이트
                            "UPDATE warehouse " +
                            "SET usingCapacity = usingCapacity + (NEW.p_quantity * product_use_capacity) " +
                            "WHERE WID = NEW.WID; " +
//            -- usingCapacity 가 totalCapacity 를 초과하는지 확인하고 초과하는 경우 롤백
                            "IF (SELECT usingCapacity FROM warehouse WHERE WID = NEW.WID) > " +
                            "(SELECT totalCapacity FROM warehouse WHERE WID = NEW.WID) THEN " +
                            "SIGNAL SQLSTATE '45000' " +
                            "SET MESSAGE_TEXT = 'Using capacity exceeds total capacity'; " +
                            "END IF; " +
                            "END IF; " +
                            "END";

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);


            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    public void receiptCreate(ReceiptVO receipt) {
        try {
            connectDB();

            String sql = "insert into receipt " +
                    "(rec_date, p_quantity, state, qrcode, approval, UID, PID, WID) " +
                    "values(now(),?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, receipt.getProductQuantity());
            pstmt.setInt(2, receipt.getState());
            pstmt.setInt(3, receipt.getQrCode());
            pstmt.setInt(4, receipt.getApproval());
            pstmt.setInt(5, receipt.getUId());
            pstmt.setInt(6, receipt.getPId());
            pstmt.setInt(7, receipt.getWId());

            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("입고 요청이 저장되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }
    }

    public void receiptUpdate(ReceiptVO receipt) {
        try {
            connectDB();
            String sql = "update receipt set " +
                    "rec_date = ?, p_quantity = ?, approval = ?, state =?, qrcode = ? " +
                    "where Rec_id = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, receipt.getReceiptDate());
            pstmt.setInt(2, receipt.getProductQuantity());
            pstmt.setInt(3, receipt.getApproval());
            pstmt.setInt(4, receipt.getState());
            pstmt.setInt(5, receipt.getQrCode());
            pstmt.setInt(6, receipt.getReceiptId());


            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("입고 요청이 수정되었습니다.");
        }catch (SQLException se) {
            if (se.getSQLState().equals("45000")) {
                System.out.println("창고 용량이 초과 되어 입고 할 수 없습니다.");
            }
        }
        catch (Exception e) {

            //e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    public void receiptDelete(ReceiptVO receipt) {       // UID 를 입력 받고
        try {
            connectDB();
            String sql = "delete from receipt where rec_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, receipt.getReceiptId());

            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }
    public void receiptCancel(ReceiptVO receipt) {       // UID 를 입력 받고
        try {
            connectDB();
            String sql = "update receipt set approval = 0 where rec_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, receipt.getReceiptId());

            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    // 관리자일 경우는 user 들의 전체적인 입고 요청들을 조회
    // 회원일 경우는 자신의 입고 요청을 전체 조회
    public List<ReceiptVO> receiptRead(UserVO userVO) {
        try {
            connectDB();
            if (userVO.getUserType() == 1) {     // userType 이 1인 경우(관리자) UID 별로 조회
                String sql = "select * from receipt";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                ResultSet rs = pstmt.executeQuery();
                receiptVOList = new ArrayList<>();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }


                for (ReceiptVO receiptVO : receiptVOList) {

                    System.out.print("UID : " + receiptVO.getUId());
                    System.out.print(" WID : " + receiptVO.getWId());
                    System.out.print(" 입고 ID : " + receiptVO.getReceiptId());
                    System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                    System.out.print(" 승인 여부 : " + receiptVO.getApproval());
                    System.out.print(" PID : " + receiptVO.getPId());
                    System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                    System.out.print(" 상태 : " + receiptVO.getState());


                    System.out.println();
                }

                rs.close();
                pstmt.close();
                return receiptVOList;


            } else if (userVO.getUserType() == 2) {       // userType 이 2인 경우 UID = ? 로 조회
                String sql = "select * from receipt where UID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, userVO.getUserID());

                ResultSet rs = pstmt.executeQuery();
                receiptVOList = new ArrayList<>();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }

                for (ReceiptVO receiptVO : receiptVOList) {
                    if (receiptVO.getUId() == userVO.getUserID()) {
                        System.out.print("UID : " + receiptVO.getUId());
                        System.out.print(" PID : " + receiptVO.getPId());
                        System.out.print(" 입고 ID : " + receiptVO.getReceiptId());
                        System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                        System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                        System.out.print(" 상태 : " + receiptVO.getState());
                        System.out.println();
                    }
                }
                rs.close();
                pstmt.close();
                return receiptVOList;
            }
            System.out.println("입고 요청을 조회했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return receiptVOList;
    }

    public void receiptPeriodRead(UserVO userVO, String firstDate, String lastDate) {       // 기간별 입고 현황 조회
        try {

            connectDB();
            if (userVO.getUserType() == 1) {     // userType 이 1인 경우(관리자) UID 별로 조회
                String sql = "select * from receipt where rec_date between ? and ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                receiptVOList = new ArrayList<>();

                pstmt.setString(1, firstDate);
                pstmt.setString(2, lastDate);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }

                for (ReceiptVO receiptVO : receiptVOList) {
                    System.out.print("입고 ID : " + receiptVO.getReceiptId());
                    System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                    System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                    System.out.print(" 상태 : " + receiptVO.getState());
                    System.out.print(" 요청 승인 여부 : " + receiptVO.getApproval());
                    System.out.println();
                }

                rs.close();
                pstmt.close();

            } else if (userVO.getUserType() == 2) {       // userType 이 2인 경우 UID = ? 로 조회
                String sql = "select * from receipt where UID = ? and rec_date between ? and ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                receiptVOList = new ArrayList<>();

                pstmt.setInt(1, userVO.getUserID());
                pstmt.setString(2, firstDate);
                pstmt.setString(3, lastDate);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }

                for (ReceiptVO receiptVO : receiptVOList) {
                    System.out.print("입고 ID : " + receiptVO.getReceiptId());
                    System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                    System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                    System.out.print(" 상태 : " + receiptVO.getState());
                    System.out.print(" 요청 승인 여부 : " + receiptVO.getApproval());
                    System.out.println();
                }


                rs.close();
                pstmt.close();

            }

            System.out.println("입고 요청을 조회했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    public void receiptMonthRead(UserVO userVO) {       // 기간별 입고 현황 조회
        try {
            connectDB();
            if (userVO.getUserType() == 1) {     // userType 이 1인 경우(관리자) UID 별로 조회
                String sql = "select * from receipt" +
                        " order by rec_date, UID";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                receiptVOList = new ArrayList<>();
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }

                for (ReceiptVO receiptVO : receiptVOList) {
                    System.out.print("입고 ID : " + receiptVO.getReceiptId());
                    System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                    System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                    System.out.print(" 상태 : " + receiptVO.getState());
                    System.out.print(" 요청 승인 여부 : " + receiptVO.getApproval());
                    System.out.println();
                }


                rs.close();
                pstmt.close();

            } else if (userVO.getUserType() == 2) {       // userType 이 2인 경우 UID = ? 로 조회
                String sql = "select * from receipt where UID = ? order by rec_date, UID ";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                receiptVOList = new ArrayList<>();
                pstmt.setInt(1, userVO.getUserID());

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    ReceiptVO receiptVO = new ReceiptVO();
                    receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                    receiptVO.setReceiptDate(rs.getString("rec_date"));
                    receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                    receiptVO.setState(rs.getInt("state"));
                    receiptVO.setQrCode(rs.getInt("qrcode"));
                    receiptVO.setApproval(rs.getInt("approval"));
                    receiptVO.setUId(rs.getInt("UID"));
                    receiptVO.setPId(rs.getInt("PID"));
                    receiptVO.setWId(rs.getInt("WID"));
                    receiptVOList.add(receiptVO);
                }

                for (ReceiptVO receiptVO : receiptVOList) {
                    System.out.print("입고 ID : " + receiptVO.getReceiptId());
                    System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                    System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                    System.out.print(" 상태 : " + receiptVO.getState());
                    System.out.print(" 요청 승인 여부 : " + receiptVO.getApproval());
                    System.out.println();
                }


                rs.close();
                pstmt.close();

            }

            System.out.println("입고 요청을 조회했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    public List<ReceiptVO> receiptReadApproval() {     // 입고 요청 승인 해주기 위해 approval = 0 인 놈들 갖고오기
        try {
            connectDB();
            String sql = "select * from receipt where approval = 0 order by rec_date, UID";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            receiptVOList = new ArrayList<>();

            while (rs.next()) {
                ReceiptVO receiptVO = new ReceiptVO();
                receiptVO.setReceiptId(rs.getInt("Rec_ID"));
                receiptVO.setReceiptDate(rs.getString("rec_date"));
                receiptVO.setProductQuantity(rs.getInt("p_quantity"));
                receiptVO.setState(rs.getInt("state"));
                receiptVO.setQrCode(rs.getInt("qrcode"));
                receiptVO.setApproval(rs.getInt("approval"));
                receiptVO.setUId(rs.getInt("UID"));
                receiptVO.setPId(rs.getInt("PID"));
                receiptVO.setWId(rs.getInt("WID"));
                receiptVOList.add(receiptVO);
            }

            for (ReceiptVO receiptVO : receiptVOList) {
                System.out.print("입고 ID : " + receiptVO.getReceiptId());
                System.out.print(" 입고 날짜 : " + receiptVO.getReceiptDate());
                System.out.printf(" 상품 수량 : %4d ", receiptVO.getProductQuantity());
                System.out.print(" 상태 : " + receiptVO.getState());
                System.out.print(" 요청 승인 여부 : " + receiptVO.getApproval());
                System.out.print(" UID :" + receiptVO.getUId());
                System.out.print(" PID :" + receiptVO.getPId());
                System.out.print(" WID :" + receiptVO.getWId());
                System.out.println();
            }


            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptVOList;
    }

}