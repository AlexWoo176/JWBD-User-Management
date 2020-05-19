package com.codegym.dao;

import com.codegym.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    void insertUser(User user);
    User selectUser(int id);
    List<User> selectAllUser();
    List<User> searchUserFollowCountry(String country);
    List<User> sortByName();
    boolean deleteUser(int id);
    boolean updateUser(User user);
    User getUserById(int id);
    void insertUserStore(User user) throws SQLException;
}
