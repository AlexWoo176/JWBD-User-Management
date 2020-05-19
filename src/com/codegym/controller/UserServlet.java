package com.codegym.controller;

import com.codegym.dao.UserDao;
import com.codegym.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    public void init() {
        userDao = new UserDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                try {
                    addUser(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "edit":
                updateUser(request, response);
                break;
            case "search":
                searchUser(request, response);
                break;
        }
    }

    private void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String country = request.getParameter("country");
        if (country.equals("")) {
            request.setAttribute("message", "vui long nhap thong tin");
            RequestDispatcher dispatcher = request.getRequestDispatcher("user/searchByCountry.jsp");
            dispatcher.forward(request, response);
        } else {
            List<User> listSearch = userDao.searchUserFollowCountry(country);
            if (listSearch.size() != 0) {
                request.setAttribute("listSearch", listSearch);
                RequestDispatcher dispatcher = request.getRequestDispatcher("user/search.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("message", "khong tim thay");
                RequestDispatcher dispatcher = request.getRequestDispatcher("user/searchByCountry.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User updateUser = new User(id, name, email, country);
        userDao.updateUser(updateUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User user = new User(name, email, country);
//        userDao.insertUser(user);
        userDao.insertUserStore(user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showNewForm(response, request);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "search":
                showSearchForm(request, response);
                break;
            case "sort":
                sortUser(request, response);
            case "permision":
                addUserPermision(request, response);
                break;
            case "test-without-tran":
                testWithoutTran(request, response);
                break;
            case "test-use-tran":
                testUseTran(request, response);
                break;
            default:
                listUser(request, response);
                break;
        }
    }

    private void testUseTran(HttpServletRequest request, HttpServletResponse response) {
        userDao.insertUpdateUseTransaction();
    }

    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        userDao.insertUpdateWithoutTransaction();
    }

    private void addUserPermision(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("Kien", "kienhoang@gmail.com","VietNam");
        int[] permision = {1, 2, 4};
        userDao.addUserTransaction(user,permision);
    }

    private void sortUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList=userDao.sortByName();
        request.setAttribute("userList",userList);
        RequestDispatcher dispatcher=request.getRequestDispatcher("user/sortbyname.jsp");
        dispatcher.forward(request,response);
    }

    private void showSearchForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher=request.getRequestDispatcher("user/searchByCountry.jsp");
        dispatcher.forward(request,response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDao.deleteUser(id);
        List<User> userList = userDao.selectAllUser();
        request.setAttribute("userList", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
//        User user = userDao.selectUser(id);
        User user = userDao.getUserById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", user);
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = userDao.selectAllUser();
        request.setAttribute("userList", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
}
