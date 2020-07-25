<%@ page import="il.ac.hit.costmanagement.model.IUserDAO" %>
<%@ page import="il.ac.hit.costmanagement.model.CostManagementDAO" %>
<%@ page import="il.ac.hit.costmanagement.exception.CostManagementException" %>
<%@ page import="il.ac.hit.costmanagement.dm.User" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %><%--
  Created by IntelliJ IDEA.
  User: Maor
  Date: 24/07/2020
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!

    private String userNameCookie = null;
    private String passwordCookie = null;
    boolean isLoginSucceed;
    IUserDAO dao = CostManagementDAO.getInstance();

%>






<%
    getCookies(request);

    System.out.println("user: " + userNameCookie );
    System.out.println("password: " + passwordCookie );

    if (userNameCookie != null && passwordCookie != null) {
        try {
           isLoginSucceed =  dao.userAuthentication(userNameCookie, passwordCookie);
           if(isLoginSucceed) {
               IUserDAO instance = CostManagementDAO.getInstance();
               User user = instance.getCurrentUser(userNameCookie);
               request.setAttribute("currentUser", user);
               request.getRequestDispatcher("costchart.jsp").forward(request, response);
               response.sendRedirect("costchart.jsp");
           }
        } catch (CostManagementException e) {
            e.printStackTrace();
        }
    }
%>



<%!

    private void getCookies(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userName"))
                userNameCookie = cookie.getValue();
            if (cookie.getName().equals("password")) {
                passwordCookie = cookie.getValue();
            }
        }

    }
%>


<html>
<head>
    <title>Title</title>
</head>
<h1>Login</h1>
<form method="get" action="login">
    Email:<input type="text" name="email"/>
    Password:<input type="text" name="password"/>
    <input type="submit" value="login"/>
</form>
</html>
