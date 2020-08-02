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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;


public class ClientController extends AbstractController {

    private  LocalDate date = new java.sql.Date(Calendar.getInstance().getTimeInMillis()).toLocalDate();
    int month = date.getMonth().getValue();
    private  JSONObject categories = new JSONObject();
    private  JSONObject spend = new JSONObject();
    private  JSONObject income = new JSONObject();
    private  JSONObject total = new JSONObject();
    private  JSONObject graphs = new JSONObject();

    private User user;

    private ISpendDAO spendDAO = CostManagementDAO.getInstance();
    private IIncomingDAO incomingDAO = CostManagementDAO.getInstance();
    private ITotalSpend totalSpendDAO =CostManagementDAO.getInstance();

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

    private  double[] monthsSpend = {januarySpend,februarySpend,marchSpend,aprilSpend,
            maySpend,juneSpend,julySpend,augustSpend,septemberSpend,octoberSpend,
            novemberSpend,decemberSpend};


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

    private double[] monthIncome = {januaryIncome,februaryIncome,marchIncome,aprilIncome,
            mayIncome,juneIncome,julyIncome,augustIncome,septemberIncome,octoberIncome,
            novemberIncome,decemberIncome};

    private double totalIncomeForMonth = 0;
    private double totalSpendForMonth = 0;
    private double totalAllCosts = 0;
    private  double incomePercent = 0;
    private  double spendPercent = 0;

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Create a new JSON object to check the authentication
            // in the RESTful web service
            JSONObject userAuthentication = new JSONObject();
            userAuthentication.put("email",email);
            userAuthentication.put("password",password);

            // Creating a new authentication request and
            // getting the response from RESTful web service
            // as a JSON object
            userAuthentication = new UsersService().checkAuthentication(userAuthentication);

            boolean isLoginSucceed = userAuthentication.getBoolean("isLoginSucceed");

            if (isLoginSucceed) {
                addCookies(response,email,password);

                JSONObject currentUser = new JSONObject();
                currentUser.put("email",email);

                // Get the current user that login
                currentUser = new UsersService().getUser(currentUser);

                user = (User)currentUser.get("currentUser");

                if(user == null)
                    response.sendRedirect(request.getContextPath()+"/err.jsp");

                request.getSession().setAttribute("currentUser", user);



                getSpendByMonth();
                getIncomeByMonth();
                getTotalAmountByMonth();



                spend.put("currentUser",user);

                spend = new HomeService().getSpendForMonth(spend);
                spendForMonth = (double) spend.get("spendForMonth");
                request.getSession().setAttribute("spendForMonth",spendForMonth);

                income.put("currentUser",user);

                income = new HomeService().getIncomeForMonth(income);
                incomeForMonth = (double) income.get("incomeForMonth");
                request.getSession().setAttribute("incomeForMonth",incomeForMonth);

                total.put("currentUser",user);

                total = new HomeService().getTotalAmount(total);
                totalAmountForMonth = (double) total.get("totalAmountForMonth");
                request.getSession().setAttribute("totalAmountForMonth",totalAmountForMonth);


                request.getRequestDispatcher("home.jsp").forward(request, response);

            }

        } catch (CostManagementException e) {
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }
    }


    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = new User(email, password, 0);

            // Create a new JSON object to create a new user
            // in the RESTful web service
            JSONObject registerNewUser = new JSONObject();
            registerNewUser.put("user", user);
            // Creating a new register request and
            // getting the response from RESTful web service
            // as a JSON object
            registerNewUser = new UsersService().registerUser(registerNewUser);

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

    public void addspend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CostManagementException {

        try {

             user = (User) request.getSession().getAttribute("currentUser");

            double amount = Double.parseDouble(request.getParameter("amount"));
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            String category = request.getParameter("category");
            boolean permanentSpend = Boolean.parseBoolean(request.getParameter("permanentspend"));
            String comment = request.getParameter("comment");
            int transactionId = 0;

            Spend spend = new Spend(user.getId(), amount, date, category, permanentSpend, comment, transactionId);

            JSONObject newSpend = new JSONObject();
            newSpend.put("newSpend", spend);

            newSpend = new ActionsService().addSpend(newSpend);

            String responseFromRest =  newSpend.getString("status");
            if(responseFromRest.equals("ERROR")){
                throw new CostManagementException(responseFromRest);
            }

            System.out.println("URI: " + request.getRequestURI());
            System.out.println("PATH: " + request.getContextPath());
             request.getRequestDispatcher("/home.jsp").include(request, response);
            System.out.println("URI: " + request.getRequestURI());
        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }



    }

    public void addincome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

             user = (User) request.getSession().getAttribute("currentUser");

            double amount = Double.parseDouble(request.getParameter("amount"));
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            boolean permanentIncome = Boolean.parseBoolean(request.getParameter("permanentincome"));
            String comment = request.getParameter("comment");
            int transactionId = 0;

            Incoming incoming = new Incoming(user.getId(), amount, date, permanentIncome, comment, transactionId);

            JSONObject newIncome = new JSONObject();
            newIncome.put("newIncome", incoming);

            newIncome = new ActionsService().addIncome(newIncome);

            String responseFromRest =  newIncome.getString("status");
            if(responseFromRest.equals("ERROR")){
                throw new CostManagementException(responseFromRest);
            }

            System.out.println("URI: " + request.getRequestURI());
            System.out.println("PATH: " + request.getContextPath());
            request.getRequestDispatcher("/home.jsp").include(request, response);
            System.out.println("URI: " + request.getRequestURI());
        }
        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }


    }
        // Getting all the spend amount by category
    public void categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

             user = (User) request.getSession().getAttribute("currentUser");

        try {
            System.out.println("Arrive categories controller");
            getAllCategories();
            System.out.println("user controller: " + user.toString());
            categories.put("currentUser", user);

            categories = new HomeService().getAllCategories(categories);

            String responseFromRest =  categories.getString("status");
            if(responseFromRest.equals("ERROR")){
                throw new CostManagementException(responseFromRest);
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



    public void graphs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            user = (User) request.getSession().getAttribute("currentUser");

            getAllYearCosts();


            graphs = new HomeService().getAllYearCosts(graphs);

            String responseFromRest =  graphs.getString("status");

            if(responseFromRest.equals("ERROR")){
                throw new CostManagementException(responseFromRest);
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


          /*  costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");


            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");*/


            incomePercent = calculatePercentage(totalIncomeForMonth,totalAllCosts);
            spendPercent = calculatePercentage(totalSpendForMonth,totalAllCosts);

            System.out.println("income per: " + incomePercent);
            System.out.println("cost per: " + spendPercent);

            incomePercent = Math.floor(incomePercent * 10) / 10;
            spendPercent = Math.floor(spendPercent * 10) / 10;

        }

        catch (CostManagementException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("err.jsp").forward(request, response);
            e.printStackTrace();
        }

    }




    private void addCookies(HttpServletResponse response,String userName,String password) {

        javax.servlet.http.Cookie userNameCookie = new javax.servlet.http.Cookie("userName",userName);
        javax.servlet.http.Cookie passwordCookie = new Cookie("password", password);
        response.addCookie(userNameCookie);
        response.addCookie(passwordCookie);

    }

    private void getAllYearCosts() throws CostManagementException {

        graphs.put("januarySpend",januarySpend);
        graphs.put("februarySpend",februarySpend);
        graphs.put("marchSpend",marchSpend);
        graphs.put("aprilSpend",aprilSpend);
        graphs.put("maySpend",maySpend);
        graphs.put("juneSpend",juneSpend);
        graphs.put("julySpend",julySpend);
        graphs.put("augustSpend",augustSpend);
        graphs.put("septemberSpend",septemberSpend);
        graphs.put("octoberSpend",octoberSpend);
        graphs.put("novemberSpend",novemberSpend);
        graphs.put("decemberSpend",decemberSpend);

        graphs.put("totalSpendForMonth",totalSpendForMonth);

        graphs.put("januaryIncome",januaryIncome);
        graphs.put("februaryIncome",februaryIncome);
        graphs.put("marchIncome",marchIncome);
        graphs.put("aprilIncome",aprilIncome);
        graphs.put("mayIncome",mayIncome);
        graphs.put("juneIncome",juneIncome);
        graphs.put("julyIncome",julyIncome);
        graphs.put("augustIncome",augustIncome);
        graphs.put("septemberIncome",septemberIncome);
        graphs.put("octoberIncome",octoberIncome);
        graphs.put("novemberIncome",novemberIncome);
        graphs.put("decemberIncome",decemberIncome);

        graphs.put("totalIncomeForMonth",totalIncomeForMonth);


        graphs.put("currentUser",user);
        graphs.put("month",month);


      /*  for(int i = 0; i<monthsSpend.length; i++){
            monthsSpend[i] = spendDAO.getSpendByMonth(user.getId(),i+1);
            if(i+1==month)
                totalSpendForMonth = monthsSpend[i];
        }



        for(int i = 0; i<monthIncome.length; i++){
            monthIncome[i] = incomingDAO.getIncomeByMonth(user.getId(),i+1);
            if(i+1==month)
                totalIncomeForMonth = monthIncome[i];
        }

        totalAllCosts = Math.abs(totalIncomeForMonth + totalSpendForMonth);*/
    }

    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    private void getTotalAmountByMonth() throws CostManagementException {
        total.put("month",month);

    }

    private void getIncomeByMonth() throws CostManagementException {
        income.put("month",month);

    }

    private void getSpendByMonth() throws CostManagementException {
        spend.put("month", month);
    }

    public void getAllCategories() throws CostManagementException {

        categories.put("Shopping",shopping);
        categories.put("Transport",transport);
        categories.put("Restaurant",restaurant);
        categories.put("Health",health);
        categories.put("Family",family);
        categories.put("Groceries",groceries);
        categories.put("Leisure",leisure);
        categories.put("Government",government);
        categories.put("Food",food);

        categories.put("user",user);

    }
}

