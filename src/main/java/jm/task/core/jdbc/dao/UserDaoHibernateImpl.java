package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255),
                last_name VARCHAR(255),
                age TINYINT,
                PRIMARY KEY (id)
            )
            """;

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";

    private static final String DELETE_ALL_USERS_HQL = "DELETE FROM User";
    private static final String GET_ALL_USERS_HQL = "FROM User";

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(CREATE_TABLE_SQL).executeUpdate();
            transaction.commit();
            System.out.println("Таблица 'users' создана (или уже существует)");
        } catch (Exception e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(DROP_TABLE_SQL).executeUpdate();
            transaction.commit();
            System.out.println("Таблица 'users' удалена (если существовала)");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                System.out.println("Пользователь с ID " + id + " удален");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(GET_ALL_USERS_HQL, User.class);
            List<User> users = query.list();
            System.out.println("Получено " + users.size() + " пользователей");
            return users;
        } catch (Exception e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery(DELETE_ALL_USERS_HQL).executeUpdate();
            transaction.commit();
            System.out.println("Таблица 'users' очищена");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }

    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}