package persist;

import com.google.common.base.Joiner;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import entity.Article;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBPersistence implements Persistence {

    private Connection connection;

    public DBPersistence() {
        connection = new DBHelper().getConnection();
    }

    public void saveArticle(Article article) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into article (id,title,content,time,tags)" +
                    " values(?,?,?,?,?);");
            pstmt.setString(1, article.getId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.setString(4, article.getDate());
            pstmt.setString(5, Joiner.on(",").join(article.getTags()));
            pstmt.execute();
        } catch (SQLException e) {
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                return;
            }
            e.printStackTrace();
        }
    }

    public int articleCount() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("select count(1) from article;");
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public boolean isChannelExist(String id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("select count(1) from channel where id = ?;");
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public boolean isArticleExist(String id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("select count(1) from article where id = ?;");
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveChannel(String id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into channel (id)" +
                    " values(?);");
            pstmt.setString(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            if (e instanceof MySQLIntegrityConstraintViolationException) {
                return;
            }
            e.printStackTrace();
        }
    }
}
