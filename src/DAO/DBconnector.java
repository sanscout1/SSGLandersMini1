package DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBconnector {
    protected Connection conn = null;
    private static final String ENV_FILE = "src/config/DB.env";
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream(ENV_FILE);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDBUrl() {
        return properties.getProperty("DB_URL");
    }

    private static String getDBUser() {
        return properties.getProperty("DB_USER");
    }

    private static String getDBPassword() {
        return properties.getProperty("DB_PASSWORD");
    }
    protected void connectDB() {
        try {
            // JDBC Driver 등록
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 환경 변수 파일에서 데이터베이스 연결 정보 가져오기
            String url = DBconnector.getDBUrl();
            String user = DBconnector.getDBUser();
            String password = DBconnector.getDBPassword();

            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void closeDB() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }
}
