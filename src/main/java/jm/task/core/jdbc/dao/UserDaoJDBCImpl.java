package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.DaoException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = Util.getConnection();
    }

    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                last_name VARCHAR(255) NOT NULL,
                age TINYINT
            )
            """;

    private static final String DROP_USERS_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String SAVE_USER_SQL = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS_SQL = "SELECT id, name, last_name, age FROM users";
    private static final String CLEAN_USERS_TABLE_SQL = "TRUNCATE TABLE users";

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

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_USERS_TABLE_SQL);
            log.info("Table 'users' dropped if existed");
        } catch (SQLException e) {
            log.error("Failed to drop 'users' table", e);
            throw new DaoException("Cannot drop users table", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            // Получаем сгенерированный ID для пользователя
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                if (rs.next()) {
                    Long generatedId = rs.getLong(1);
                    log.info("User с именем – {} {} добавлен в базу данных с ID = {}",
                            name, lastName, generatedId);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to save user: {} {}", name, lastName, e);
            throw new DaoException("Cannot save user", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("User with ID = {} removed successfully", id);
            } else {
                log.warn("No user found with ID = {}", id);
            }
        } catch (SQLException e) {
            log.error("Failed to remove user with ID = {}", id, e);
            throw new DaoException("Cannot remove user by id", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }

            log.info("Retrieved {} users from database", users.size());
        } catch (SQLException e) {
            log.error("Failed to get all users", e);
            throw new DaoException("Cannot get all users", e);
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_USERS_TABLE_SQL);
            log.info("Table 'users' cleaned (all data removed)");
        } catch (SQLException e) {
            log.error("Failed to clean 'users' table", e);
            throw new DaoException("Cannot clean users table", e);
        }
    }
}