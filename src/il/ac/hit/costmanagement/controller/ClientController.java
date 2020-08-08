package il.ac.hit.costmanagement.controller;



import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.model.*;
import il.ac.hit.costmanagement.rest.ActionsService;
import il.ac.hit.costmanagement.rest.HomeService;
import org.json.*;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.rest.UsersService;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * This class extends Abstract controller.
 * A new request that come from the RouterServlet
 * is redirect to RESTful web service.
 */
public class ClientController extends AbstractController {

    private final static String ERROR_FROM_RESTFUL = "ERROR";
    private  LocalDate date = new java.sql.Date(Calendar.getInstance().getTimeInMillis()).toLocalDate();
    int month = date.getMonth().getValue();
    private  JSONObject categories = new JSONObject();
    private  JSONObject spend = new JSONObject();
    private  JSONObject income = new JSONObject();
    private  JSONObject total = new JSONObject();
    private  JSONObject graphs = new JSONObject();

    private User user;


    private double shopping = 0;
    private double transport = 0;
    private double restaurant = 0;
    private double health = 0;
    private double family = 0;
    private double groceries = 0;
    private double leisure = 0;
    private double government = 0;
    private double food = 0;

    private double totalAmountForMonth = 0;
    private double spendForMonth = 0;
    private  double incomeForMonth = 0;

    private double januarySpend = 0;
    private double februarySpend = 0;
    private double marchSpend = 0;
    private double aprilSpend = 0;
    private double maySpend = 0;
    private double juneSpend = 0;
    private double julySpend = 0;
    private double augustSpend = 0;
    private double septemberSpend = 0;
    private double octoberSpend = 0;
    private double novemberSpend = 0;
    private double decemberSpend = 0;


    private double januaryIncome = 0;
    private double februaryIncome = 0;
    private double marchIncome = 0;
    private  double aprilIncome = 0;
    private  double mayIncome = 0;
    private double juneIncome = 0;
    private double julyIncome = 0;
    private double augustIncome = 0;
    private  double septemberIncome = 0;
    private double octoberIncome = 0;
    private double novemberIncome = 0;
    private  double decemberIncome = 0;

    private double totalIncomeForMonth = 0;
    private double totalSpendForMonth = 0;
    private double totalAllCosts = 0;
    private  double incomePercent = 0;
    private  double spendPercent = 0;

    /**
     * Checks the authentication and query the income and spend from db.
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws IOException
     * @throws ServletException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CostManagementException {

        /*****/

        JSONObject test = new JSONObject();
        test.put("email","maorytest123@gmail.com");
        test.put("password","12345");

        Response response1 =  new UsersService().reg(test.toString());
        JSONObject jsonObject = (JSONObject) response1.getEntity();
        System.out.println("login controller : " + jsonObject.getString("email") + "pass: " + jsonObject.getString("password"));


        /************/

        String email = request.getParameter("email");
        String password = request.getParameter("password");


        // Create a new JSON object to check the authentication
        // in the RESTful web service
        JSONObject userAuthentication = new JSONObject();
        userAuthentication.put("email",email);
        userAuthentication.put("password",password);

        // Creating a new authentication request and
        // getting the response from RESTful web service
        // as a JSON object
        userAuthentication = new UsersService().checkAuthentication(userAuthentication.toString());
        if(userAuthentication.get("status").equals(ERROR_FROM_RESTFUL))
            response.sendRedirect(request.getContextPath()+"/err.jsp");

        boolean isLoginSucceed = userAuthentication.getBoolean("isLoginSucceed");

        if (isLoginSucceed) {
            addCookies(response,email,password);

            JSONObject currentUser = new JSONObject();
            currentUser.put("email",email);

            // Get the current user that login
            currentUser = new UsersService().getUser(currentUser.toString());

            if(currentUser.get("status").equals(ERROR_FROM_RESTFUL))
                response.sendRedirect(request.getContextPath()+"/err.jsp");

            user = (User)currentUser.get("currentUser");

            if(user == null)
                response.sendRedirect(request.getContextPath()+"/err.jsp");

            request.getSession().setAttribute("currentUser", user);

            spend.put("currentUser",user);
            spend.put("month",month);

            spend = new HomeService().getSpendForMonth(spend);

            if(spend.get("status").equals(ERROR_FROM_RESTFUL))
                response.sendRedirect(request.getContextPath()+"/err.jsp");

            spendForMonth = (double) spend.get("spendForMonth");
            request.getSession().setAttribute("spendForMonth",spendForMonth);

            income.put("currentUser",user);
            income.put("month",month);

            income = new HomeService().getIncomeForMonth(income);

            if(income.get("status").equals(ERROR_FROM_RESTFUL))
                response.sendRedirect(request.getContextPath()+"/err.jsp");

            incomeForMonth = (double) income.get("incomeForMonth");
            request.getSession().setAttribute("incomeForMonth",incomeForMonth);

            total.put("currentUser",user);
            total.put("month",month);

            total = new HomeService().getTotalAmount(total);

            if(total.get("status").equals(ERROR_FROM_RESTFUL))
                response.sendRedirect(request.getContextPath()+"/err.jsp");

            totalAmountForMonth = (double) total.get("totalAmountForMonth");
            request.getSession().setAttribute("totalAmountForMonth",totalAmountForMonth);


           request.getRequestDispatcher("home.jsp").forward(request, response);

        }


    }


    /**
     * Register a new user
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws IOException
     * @throws ServletException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            //User user = new User(email, password, 0);

            // Create a new JSON object to create a new user
            // in the RESTful web service
            JSONObject registerNewUser = new JSONObject();
            registerNewUser.put("email", email);
            registerNewUser.put("password",password);
            // Creating a new register request and
            // getting the response from RESTful web service
            // as a JSON object
          //  registerNewUser = new UsersService().registerUser(registerNewUser.toString());

            if(registerNewUser.get("status").equals(ERROR_FROM_RESTFUL))
                throw new CostManagementException(registerNewUser.getString("errorMessage"));

            boolean isRegisterSucceed = registerNewUser.getBoolean("isRegistrationSucceed");

            if (isRegisterSucceed) {
                PrintWriter writer = response.getWriter();
                writer.print("Registered Succeed");
            }
        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }

    }


    /**
     * Add a new spend to the currently month
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws ServletException
     * @throws IOException
     */
    public void addspend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

             user = (User) request.getSession().getAttribute("currentUser");

            double amount = Double.parseDouble(request.getParameter("amount"));
            String category = request.getParameter("category");
            boolean permanentSpend = Boolean.parseBoolean(request.getParameter("permanentspend"));
            String comment = request.getParameter("comment");
           // int transactionId = 0;

            //Spend spend = new Spend(user.getId(), amount, date, category, permanentSpend, comment, transactionId);

            JSONObject newSpend = new JSONObject();
           // newSpend.put("newSpend", spend);
            newSpend.put("userId",user.getId());
            newSpend.put("amount",amount);
            newSpend.put("category",category);
            newSpend.put("permanentSpend",permanentSpend);
            newSpend.put("comment",comment);



            newSpend = new ActionsService().addSpend(newSpend.toString());

            if(newSpend.getString("status").equals(ERROR_FROM_RESTFUL)){
                throw new CostManagementException(newSpend.getString("errorMessage"));
            }

             request.getRequestDispatcher("/home.jsp").include(request, response);
        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }



    }

    /**
     * Add a new income to the currently month
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws ServletException
     * @throws IOException
     */
    public void addincome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

             user = (User) request.getSession().getAttribute("currentUser");

            double amount = Double.parseDouble(request.getParameter("amount"));
           // Date date = new Date(Calendar.getInstance().getTimeInMillis());
            boolean permanentIncome = Boolean.parseBoolean(request.getParameter("permanentincome"));
            String comment = request.getParameter("comment");
           // int transactionId = 0;

           // Incoming incoming = new Incoming(user.getId(), amount, date, permanentIncome, comment, transactionId);

            JSONObject newIncome = new JSONObject();
            //newIncome.put("newIncome", incoming);
            newIncome.put("currentUser",user.getId());
            newIncome.put("amount",amount);
            newIncome.put("permanentIncome",permanentIncome);
            newIncome.put("comment",comment);

            newIncome = new ActionsService().addIncome(newIncome.toString());


            if(newIncome.getString("status").equals(ERROR_FROM_RESTFUL)){
                throw new CostManagementException(newIncome.getString("errorMessage"));
            }

            request.getRequestDispatcher("/home.jsp").include(request, response);

        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Getting all the categories that spend of the currently month
     * @param request an object that represent the request
     *  @param response and object that represent the response
     * @throws ServletException
     * @throws IOException
     */
    public void categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

             user = (User) request.getSession().getAttribute("currentUser");

        try {

           // categories.put("month",month);
            categories.put("currentUser", user.getId());

            categories = new HomeService().getAllCategories(categories.toString());

            if(categories.getString("status").equals(ERROR_FROM_RESTFUL)){
                throw new CostManagementException(categories.getString("errorMessage"));
            }

            shopping = (double) categories.get("Shopping");
            transport = (double) categories.get("Transport");
            restaurant = (double) categories.get("Restaurant");
            health = (double) categories.get("Health");
            family = (double) categories.get("Family");
            groceries = (double) categories.get("Groceries");
            leisure = (double) categories.get("Leisure");
            government = (double) categories.get("Government");
            food = (double) categories.get("Food");


            request.getSession().setAttribute("shopping", shopping);
            request.getSession().setAttribute("transport", transport);
            request.getSession().setAttribute("restaurant", restaurant);
            request.getSession().setAttribute("health", health);
            request.getSession().setAttribute("family", family);
            request.getSession().setAttribute("groceries", groceries);
            request.getSession().setAttribute("leisure", leisure);
            request.getSession().setAttribute("government", government);
            request.getSession().setAttribute("food", food);

            request.getRequestDispatcher("/categories.jsp").include(request, response);

        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }

    }


    /**
     * Getting all the costs of the current year to make a graph of the year
     * and getting all the costs of the currently month
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws ServletException
     * @throws IOException
     */
    public void graphs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            user = (User) request.getSession().getAttribute("currentUser");

            graphs.put("currentUser",user.getId());
            graphs.put("month",month);

            graphs = new HomeService().getAllYearCosts(graphs.toString());


            if(graphs.getString("status").equals(ERROR_FROM_RESTFUL)){
                throw new CostManagementException(graphs.getString("errorMessage"));
            }


             januarySpend =  (double)graphs.get("januarySpend");
             februarySpend = (double)graphs.get("februarySpend");
             marchSpend = (double)graphs.get("marchSpend");
             aprilSpend = (double)graphs.get("aprilSpend");
             maySpend = (double)graphs.get("maySpend");
            juneSpend = (double)graphs.get("juneSpend");
            julySpend = (double)graphs.get("julySpend");
            augustSpend = (double)graphs.get("augustSpend");
            septemberSpend = (double)graphs.get("septemberSpend");
            octoberSpend = (double)graphs.get("octoberSpend");
            novemberSpend = (double)graphs.get("novemberSpend");
            decemberSpend = (double)graphs.get("decemberSpend");

            totalSpendForMonth = (double)graphs.get("totalSpendForMonth");

            januaryIncome = (double)graphs.get("januaryIncome");
            februaryIncome = (double)graphs.get("februaryIncome");
            marchIncome = (double)graphs.get("marchIncome");
            aprilIncome = (double)graphs.get("aprilIncome");
            mayIncome = (double)graphs.get("mayIncome");
            juneIncome = (double)graphs.get("juneIncome");
            julyIncome = (double)graphs.get("julyIncome");
            augustIncome = (double)graphs.get("augustIncome");
            septemberIncome = (double)graphs.get("septemberIncome");
            octoberIncome = (double)graphs.get("octoberIncome");
            novemberIncome = (double)graphs.get("novemberIncome");
            decemberIncome = (double)graphs.get("decemberIncome");

            totalIncomeForMonth = (double)graphs.get("totalIncomeForMonth");

            totalAllCosts = totalIncomeForMonth + totalSpendForMonth;

            request.getSession().setAttribute("januarySpend",januarySpend);
            request.getSession().setAttribute("februarySpend",februarySpend);
            request.getSession().setAttribute("marchSpend",marchSpend);
            request.getSession().setAttribute("aprilSpend",aprilSpend);
            request.getSession().setAttribute("maySpend",maySpend);
            request.getSession().setAttribute("juneSpend",juneSpend);
            request.getSession().setAttribute("julySpend",julySpend);
            request.getSession().setAttribute("augustSpend",augustSpend);
            request.getSession().setAttribute("septemberSpend",septemberSpend);
            request.getSession().setAttribute("octoberSpend",octoberSpend);
            request.getSession().setAttribute("novemberSpend",novemberSpend);
            request.getSession().setAttribute("decemberSpend",decemberSpend);

            request.getSession().setAttribute("totalSpendForMonth",totalSpendForMonth);


            request.getSession().setAttribute("januaryIncome",januaryIncome);
            request.getSession().setAttribute("februaryIncome",februaryIncome);
            request.getSession().setAttribute("marchIncome",marchIncome);
            request.getSession().setAttribute("aprilIncome",aprilIncome);
            request.getSession().setAttribute("mayIncome",mayIncome);
            request.getSession().setAttribute("juneIncome",juneIncome);
            request.getSession().setAttribute("julyIncome",julyIncome);
            request.getSession().setAttribute("augustIncome",augustIncome);
            request.getSession().setAttribute("septemberIncome",septemberIncome);
            request.getSession().setAttribute("octoberIncome",octoberIncome);
            request.getSession().setAttribute("novemberIncome",novemberIncome);
            request.getSession().setAttribute("decemberIncome",decemberIncome);

            request.getSession().setAttribute("totalIncomeForMonth",totalIncomeForMonth);

            request.getSession().setAttribute("totalAllCosts",totalAllCosts);

            incomePercent = calculatePercentage(totalIncomeForMonth,totalAllCosts);
            spendPercent = calculatePercentage(totalSpendForMonth,totalAllCosts);

            incomePercent = Math.floor(incomePercent * 10) / 10;
            spendPercent = Math.floor(spendPercent * 10) / 10;

            request.getSession().setAttribute("incomePercent",incomePercent);
            request.getSession().setAttribute("spendPercent",spendPercent);

            request.getRequestDispatcher("/graphs.jsp").include(request, response);

        }

        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }

    }

    /**
     * logout the current user and invalidate his session and erase the cookies
     * @param request an object that represent the request
     * @param response and object that represent the response
     * @throws ServletException
     * @throws IOException
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        user = (User) request.getSession().getAttribute("currentUser");
        if(user != null){
            request.getSession().invalidate();
            eraseCookies(request, response);
            request.getRequestDispatcher("/logout.jsp").include(request, response);
        }
    }

    /**
     * This function erases the cookies from the current user
     * @param req an object that represent the request
     * @param resp and object that represent the response
     */
    private void eraseCookies(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }


    }


    /**
     * This function add cookies 'userName' and 'password' for the user
     * for an auto login
     * @param response an object that represent the response
     * @param userName an object the represent the user name of the user
     * @param password an object the represent the password of the user
     */
    private void addCookies(HttpServletResponse response,String userName,String password) {

        javax.servlet.http.Cookie userNameCookie = new javax.servlet.http.Cookie("userName",userName);
        javax.servlet.http.Cookie passwordCookie = new Cookie("password", password);
        response.addCookie(userNameCookie);
        response.addCookie(passwordCookie);
    }


    /**
     * This function calculate the percentage of the income and spend for the current month
     * @param obtained the income object or spend object
     * @param total the total sum of income and spend
     * @return the percentage of the obtained object
     */
    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }






}

