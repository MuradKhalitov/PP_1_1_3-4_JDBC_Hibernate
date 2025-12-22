package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        UserDao userDao = new UserDaoJDBCImpl();
        UserService userService = new UserServiceImpl(userDao);
        userService.createUsersTable();
    }
}
