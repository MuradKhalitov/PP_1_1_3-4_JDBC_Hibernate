package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.config.JDBCConfig;
import jm.task.core.jdbc.exception.DaoException;
import jm.task.core.jdbc.model.User;
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

    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                last_name VARCHAR(255) NOT NULL,
                age TINYINT
            )
            """;

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_USERS_TABLE_SQL);
            log.info("Table 'users' created or already exists");
        } catch (SQLException e) {
            log.error("Failed to create 'users' table", e);
            throw new DaoException("Cannot create users table", e);
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
