<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 19/5/2020
  Time: 11:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management Application @AlexWoo 2020</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2><a href="/users?action=create">Add New User</a></h2>
    <h2><a href="/users?action=search">Search By Country</a></h2>
    <h2><a href="/users?action=sort"> Sort By Name</a></h2>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Users</h2></caption>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Country</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="user" items="${userList}">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td><c:out value="${user.country}"/></td>
                <td>
                    <a href="/users?action=edit&id=${user.id}">Edit</a>
                </td>
                <td><a href="/users?action=delete&id=${user.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
