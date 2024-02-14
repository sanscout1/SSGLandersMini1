package DAO;

import VO.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends DBconnector {
    private static volatile UserDao instance;

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    //DB 열기

    // 1. 로그인
    public UserVO loginUser(String ID, String password) {
        UserVO user = null;
        connectDB();

        String sql = "SELECT * FROM user WHERE id = ? AND password = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new UserVO(
                        rs.getInt("uid"),
                        rs.getInt("utype"),
                        rs.getString("ID"),
                        rs.getString("password"),
                        rs.getString("uname"),
                        rs.getString("taxID"),
                        rs.getString("address_city"),
                        rs.getInt("address_city_Num"),
                        rs.getBoolean("approval"));

            }
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return user;
    }

    // 2. 회원가입
    public boolean registerUser(int utype, String ID, String password, String userName, String taxID, String addressCity, int addressNum) {
        try {
            // 중복된 아이디 확인
            if (isIdDuplicate(ID)) {
                System.out.println("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
                return false;
            }
            // 중복된 사업자 등록 번호 확인
            if (isTaxIdDuplicate(taxID)) {
                System.out.println("이미 등록된 사업자 등록 번호입니다. 다른 번호를 입력해주세요.");
                return false;
            }

            connectDB();
            String sql = "INSERT INTO user (utype, ID, password, uname, taxID, address_city, address_city_num, approval) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, utype);
            pstmt.setString(2, ID);
            pstmt.setString(3, password);
            pstmt.setString(4, userName);
            pstmt.setString(5, taxID);
            pstmt.setString(6, addressCity);
            pstmt.setInt(7, addressNum);
            pstmt.setBoolean(8, utype == 1);

            int ack = pstmt.executeUpdate();

            pstmt.close();
            closeDB();
            return ack > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 중복된 아이디인지 확인하는 메서드
    public boolean isIdDuplicate(String ID) {
        try {
            connectDB();
            String sql = "SELECT COUNT(*) FROM user WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            rs.close();
            pstmt.close();
            closeDB();
            return count > 0; // count가 0보다 크면 중복된 아이디
        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        }
    }

    // 중복된 사업자 등록 번호인지 확인하는 메서드
    public boolean isTaxIdDuplicate(String taxID) {
        try {
            connectDB();
            String sql = "SELECT COUNT(*) FROM user WHERE taxID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxID);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            rs.close();
            pstmt.close();
            closeDB();
            return count > 0; // count가 0보다 크면 중복된 사업자 등록 번호
        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        }
    }

    // 3. 아이디 찾기
    public String findUserID(String taxID) {
        connectDB();

        String foundID = null;
        String sql = "SELECT ID FROM user WHERE taxID = ?";


        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxID);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                foundID = rs.getString("ID");
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return foundID;
    }

    // 4. 비밀번호 찾기
    public String findUserPassword(String id) {
        connectDB();

        String foundPassword = null;
        String sql = "SELECT password FROM user WHERE ID = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                foundPassword = rs.getString("password");

            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return foundPassword;
    }

    // 관리자 목록 메인
    // 1. 조회
    public UserVO getUserId(String ID) {
        UserVO user = null;
        connectDB();

        String sql = "SELECT * FROM user WHERE ID =?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserVO(
                            rs.getInt("uid"),
                            rs.getInt("utype"),
                            rs.getString("ID"),
                            rs.getString("password"),
                            rs.getString("uname"),
                            rs.getString("taxID"),
                            rs.getString("address_city"),
                            rs.getInt("address_city_Num"),
                            rs.getBoolean("approval"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return user;
    }

    // user List 읽기
    public List<UserVO> userRead() {
        List<UserVO> userVOList = new ArrayList<>();
        try {
            connectDB();
            String sql = "SELECT * FROM user";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                UserVO userVO = new UserVO(
                        rs.getInt("uid"),
                        rs.getInt("utype"),
                        rs.getString("ID"),
                        rs.getString("password"),
                        rs.getString("uname"),
                        rs.getString("taxID"),
                        rs.getString("address_city"),
                        rs.getInt("address_city_Num"),
                        rs.getBoolean("approval"));
                userVOList.add(userVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }
        return userVOList;
    }

    // 2-1 회원 정보 수정
    public boolean updateUser(String ID, int utype, String password, String userName, String addressCity, int addressNum, boolean approval) {
        try {
            connectDB();
            String sql = "UPDATE user SET utype=?, password=?, uname=?, address_city=?, address_city_num=?, approval=? WHERE ID=?";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, utype);
            pstmt.setString(2, password);
            pstmt.setString(3, userName);
            pstmt.setString(4, addressCity);
            pstmt.setInt(5, addressNum);
            pstmt.setBoolean(6, approval);
            pstmt.setString(7, ID);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }

    // 2-2 쇼핑몰 사업자 정보 수정
    public boolean taxIdUpdate(String ID, String taxID) {
        try {
            connectDB();
            String sql = "UPDATE user SET taxID=? WHERE ID=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxID);
            pstmt.setString(2, ID);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }

    // 3. 삭제
    public boolean memberDelete(String id) {
        try {
            connectDB();
            String sql = "DELETE FROM user WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }

    // 회원 목록 메인
    // 2-1 회원 정보 수정
    public boolean myInfoUpdate(String ID, String password, String userName, String addressCity, int addressNum) {
        try {
            connectDB();
            String sql = "UPDATE user SET password=?, uname=?, address_city=?, address_city_num=? WHERE ID=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setString(2, userName);
            pstmt.setString(3, addressCity);
            pstmt.setInt(4, addressNum);
            pstmt.setString(5, ID);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }

    // 2-2 사업자 등록 번호 수정
    public boolean mytaxIdUpdate(String password, String taxID) {
        try {
            connectDB();
            String sql = "UPDATE user SET taxID=? WHERE password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taxID);
            pstmt.setString(2, password);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }

    // 3. 회원 탈퇴
    public boolean userDelete(String password) {
        try {
            connectDB();
            String sql = "DELETE FROM user WHERE password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, password);

            int ack = pstmt.executeUpdate();
            pstmt.close();

            return ack > 0;

        } catch (SQLException e) {
            throw new RuntimeException("SQL 문장 실행 중 오류 발생", e);
        } catch (Exception e) {
            closeDB();
            e.printStackTrace();
            return false;
        }
    }
}

