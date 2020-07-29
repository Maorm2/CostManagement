package il.ac.hit.costmanagement.view;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "/login",urlPatterns = {"/login"})
public class Login extends HttpServlet {

    boolean isLoginSucceed;
    IUserDAO dao = CostManagementDAO.getInstance();

    public Login() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("text/html");

            checkAuthentication(request,response);
    }

    private void checkAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            isLoginSucceed = dao.userAuthentication(email, password);
            if (isLoginSucceed) {
                addCookies(response,email,password);
                User user = dao.getCurrentUser(email);
                request.setAttribute("currentUser", user);
                request.getRequestDispatcher("home.jsp").forward(request, response);
              //  response.sendRedirect("home.jsp");
                /*request.getRequestDispatcher("costchart.jsp").forward(request, response);
                response.sendRedirect("costchart.jsp");*/
                
            }

        } catch (CostManagementException | ServletException | IOException e) {
            PrintWriter writer = response.getWriter();
            writer.print(e.getMessage());
            e.printStackTrace();
        }

    }


    private void addCookies(HttpServletResponse response,String userName,String password) {

    Cookie userNameCookie = new Cookie("userName",userName);
    Cookie passwordCookie = new Cookie("password", password);
    response.addCookie(userNameCookie);
    response.addCookie(passwordCookie);

    }

}

