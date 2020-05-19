package com.codegym.dao;

import com.codegym.models.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
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
    private static final String SQL_INSERT = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE EMPLOYEE"
            + "("
            + " ID serial auto_increment,"

            + " NAME varchar(100) NOT NULL,"

            + " SALARY numeric(15, 2) NOT NULL,"

            + " CREATED_DATE timestamp,"

            + " PRIMARY KEY (ID)"

            + ")";
    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS EMPLOYEE";

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

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{CALL get_user_by_id(?)}";
        try (
                Connection connection = getConnection();
                CallableStatement callableStatement = connection.prepareCall(query);
        ) {
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {

                String name = resultSet.getString("name");

                String email = resultSet.getString("email");

                String country = resultSet.getString("country");

                user = new User(id, name, email, country);

            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return user;
    }

    @Override
    public void insertUserStore(User user) throws SQLException {

        String query = "{CALL insert_user(?,?,?)}";

        try
                (Connection connection = getConnection();
                 CallableStatement callableStatement = connection.prepareCall(query);) {
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());
            System.out.println(callableStatement);
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void addUserTransaction(User user, int[] permisions) {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstmAssignment = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            pstm = connection.prepareStatement(Insert_User_Sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getCountry());

            int rowAffected = pstm.executeUpdate();
            resultSet = pstm.getGeneratedKeys();
            int userId = 0;
            if (resultSet.next())
                userId = resultSet.getInt(1);

            if (rowAffected == 1) {
                String sqlPivot = "INSERT INTO user_permision(user_id,permision_id) " + "VALUES(?,?)";
                pstmAssignment = connection.prepareStatement(sqlPivot);

                for (int permisionId : permisions) {
                    pstmAssignment.setInt(1, userId);
                    pstmAssignment.setInt(2, permisionId);
                    pstmAssignment.executeUpdate();
                }
                connection.commit();
            } else {
                connection.rollback();
            }

        } catch (SQLException ex) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (pstm != null)
                    pstm.close();
                if (pstmAssignment != null)
                    pstmAssignment.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE))
        {
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            psInsert.setString(1, "Quynh Bup Be");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1, "Ngan 98");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psUpdate.setBigDecimal(2, new BigDecimal(999.99));

            psUpdate.setString(2, "Quynh");
            psUpdate.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
