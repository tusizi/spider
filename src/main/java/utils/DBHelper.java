package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    public static final String url = "jdbc:mysql://localhost/spider?useUnicode=true&characterEncoding=UTF-8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";

    private Connection conn = null;

    public DBHelper() {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void close() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
