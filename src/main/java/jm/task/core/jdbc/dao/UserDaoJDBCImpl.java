package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.config.JDBCConfig;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = JDBCConfig.getConnection();
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "last_name VARCHAR(255) NOT NULL, " +
                "age TINYINT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Table 'users' created.");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void dropUsersTable() {

    }

    public void saveUser(String name, String lastName, byte age) {

    }

    public void removeUserById(long id) {

    }

    public List<User> getAllUsers() {
        return null;
    }

    public void cleanUsersTable() {

    }
}
