package il.ac.hit.costmanagement.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "/error",urlPatterns = {"/error"})
public class Error extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        String error = (String)request.getAttribute("error");

        switch (error){
            case "User name is already exists":
                printRegisterError(out);
                break;
            case "Username or password is incorrect":
                printLoginFailed(response,request,out);
                break;
            default:
                printDefaultError(response,request,out);
        }
    }

    private void printDefaultError(HttpServletResponse response, HttpServletRequest request, PrintWriter out) throws ServletException, IOException {
        out.print("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<script>\n" +
                "  alert(\"Something went wrong!\");\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>"
        );

        request.getRequestDispatcher("login.jsp").forward(request, response);

    }

    private void printLoginFailed(HttpServletResponse response, HttpServletRequest request, PrintWriter out) throws ServletException, IOException {
        out.print("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<script>\n" +
                "  alert(\"Username or password is incorrect!\");\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>"
        );

        request.getRequestDispatcher("login.jsp").forward(request, response);

    }

    private void printRegisterError(PrintWriter out) {

        out.println("<script type=\"text/javascript\">");
        out.println("alert('User name is already exists!');");
        out.println("location='register.jsp';");
        out.println("</script>");

    }
}
