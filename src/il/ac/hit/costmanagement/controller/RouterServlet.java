package il.ac.hit.costmanagement.controller;

import il.ac.hit.costmanagement.exception.CostManagementException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.ws.rs.Path;


import java.io.*;

/**
 * Servlet implementation class RouterServlet
 */

@WebServlet("/controller/*")
public class RouterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RouterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            response.setContentType("text/html");
            // TODO Auto-generated method stub
            // response.getWriter().append("Served at: ").append(request.getContextPath());
            PrintWriter out = response.getWriter();
            String text = request.getRequestURI();
            //out.print(text);

            // extracting the controller name
            String[] texts = text.split("/");
            /*
             * out.print("<br/>0... "+texts[0]); out.print("<br/>1... "+texts[1]);
             * out.print("<br/>2... "+texts[2]); out.print("<br/>3... "+texts[3]);
             * out.print("<br/>4... "+texts[4]);
             */
            System.out.println("request.getRequestURI(): " + request.getRequestURI());
            System.out.println("testController = " + texts[0]);
            System.out.println("testSAction = " + texts[1]);
            for(String text1 : texts){
                System.out.println("text: " + text1);
            }
          //  System.out.println("texts: " + texts.toString());

            // extracting controller and action
            String controller = texts[0];
            String action = texts[2];
            String data = null;
            if(texts.length>5) {
                data = texts[5];
            }


            // building the full qualified name of the controller
            String temp = controller + "ClientController";
            String controllerClassName = il.ac.hit.costmanagement.Settings.CONTROLLERS_PACKAGE + "."
                    + temp.substring(0, 1).toUpperCase() + temp.substring(1);
            System.out.println("<h1> controller full qualified name: " + controllerClassName + "</h1>");
            System.out.println("<h1> action name: " + action + "</h1>");

            // instantiating the controller class and calling
            // the action method on the controller object
            Class clazz = Class.forName(controllerClassName);
            Method method = clazz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(clazz.newInstance(), request, response);

            // creating a RequestDispatcher object that points at the JSP document
            // which is view of our action

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/" + action + ".jsp");

            dispatcher.include(request,response);




        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
