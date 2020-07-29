package il.ac.hit.costmanagement.view;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "/register",urlPatterns = {"/register"})
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/controller/");
        dispatcher.include(request,response);


        /* response.setContentType("text/html");
          String email = request.getParameter("email");
          String password = request.getParameter("password");
          IUserDAO dao = CostManagementDAO.getInstance();
          try { dao.registerUser(new User(email,password,9));
              PrintWriter writer = response.getWriter();
          writer.print("Registered Succeed");

         } catch (CostManagementException e)
          {
              PrintWriter writer = response.getWriter();
              writer.print(e.getMessage());
              e.printStackTrace();
          }*/
    }
}
