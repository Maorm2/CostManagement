package il.ac.hit.costmanagement.controller;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ClientController extends AbstractController {

    public void login(HttpServletRequest request, HttpServletResponse response){

    }


    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        IUserDAO dao = CostManagementDAO.getInstance();

        response.setContentType("text/html");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            dao.registerUser(new User(email,password,0));
            PrintWriter writer = response.getWriter();
            writer.print("Registered Succeed");
        }
        catch (CostManagementException | IOException e)
        {
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);

            e.printStackTrace();
        }

    }

    public void home(HttpServletRequest request, HttpServletResponse response){

    }

    public void costChart(HttpServletRequest request, HttpServletResponse response){



    }
}

