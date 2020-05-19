package com.codegym.dao;

import com.codegym.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {
    public UserDao() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String jdbcURL = "jdbc:mysql://localhost:3306/demo";
    private String jdbcUser = "root";
    private String jdbcPassword = "123456";

    private static final String Insert_User_Sql = "insert into users" + "(userName,email,country)values" + "(?,?,?)";
    private static final String Insert_User_By_Id = "select id,userName,email,country from users where id=?";
    private static final String Search_User_By_Country = "select*from users where country=?";
    private static final String Select_All_Users = "select*from users";
    private static final String Delete_Users = "delete from users where id=?";
    private static final String Update_Users = "update users set userName=?,email=?,country=? where id=?";
    private static final String Sort_By_Name = "select * from users order by userName";

    @Override
    public void insertUser(User user) {
        System.out.println(Insert_User_Sql);
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Insert_User_Sql);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getCountry());
            System.out.println(pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Insert_User_By_Id);
            pstm.setInt(1, id);
            System.out.println(pstm);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");

                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Select_All_Users);
            System.out.println(pstm);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> searchUserFollowCountry(String country) {
        List<User> listSearch = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Search_User_By_Country);
            pstm.setString(1, country);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                listSearch.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listSearch;
    }

    @Override
    public List<User> sortByName() {
        List<User> userList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Sort_By_Name);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("userName");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                userList.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDelete = false;
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Delete_Users);
            pstm.setInt(1, id);
            rowDelete = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDelete;
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdate = false;
        try {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(Update_Users);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getCountry());
            pstm.setInt(4, user.getId());
            rowUpdate = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowUpdate;
    }

    private void printSQLException(SQLException sex) {
        for (Throwable throwable : sex) {
            if (throwable instanceof SQLException) {
                sex.printStackTrace(System.err);
                System.err.println("SQL State:" + ((SQLException) sex).getSQLState());
                System.err.println("Error Code:" + ((SQLException) sex).getErrorCode());
                System.err.println("Message:" + ((SQLException) sex).getMessage());
                Throwable t = sex.getCause();
                while (t != null) {
                    System.out.println("Cause" + t);
                    t = t.getCause();
                }
            }
        }
    }
}
