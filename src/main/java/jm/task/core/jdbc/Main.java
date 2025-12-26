package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        // Создание таблицы
        userService.createUsersTable();

        // Сохранение пользователей
        userService.saveUser("Ivan", "Ivanov", (byte) 25);
        userService.saveUser("Petr", "Petrov", (byte) 30);
        userService.saveUser("Maria", "Sidorova", (byte) 28);
        userService.saveUser("Anna", "Smirnova", (byte) 35);

        // Получение всех пользователей и вывод в консоль
        List<User> users = userService.getAllUsers();
        users.forEach(System.out::println);

        // Очистка таблицы
        userService.cleanUsersTable();

        // Удаление таблицы
        userService.dropUsersTable();
    }
}
