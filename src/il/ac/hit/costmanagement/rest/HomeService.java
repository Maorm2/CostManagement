package il.ac.hit.costmanagement.rest;

import com.google.gson.JsonObject;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import il.ac.hit.costmanagement.model.ITotalSpend;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/home")
public class HomeService {

    ISpendDAO spendDAO = CostManagementDAO.getInstance();
    ITotalSpend totalSpendDAO = CostManagementDAO.getInstance();

    /**
     * Getting all the costs of the current year
     * @param allYearCost JSON object that represent data from request and the data of response
     * @return JSON object that represent the costs of each month of the currently year
     */
    @GET
    @Path("/yearcosts")
    @Consumes({"application/json"})
    public JSONObject getAllYearCosts(String allYearCost) {
        JSONObject response = new JSONObject(allYearCost);

        int userId = (int) response.get("currentUser");
        int month = (int) response.get("month");

        double totalSpendForMonth = 0;
        double totalIncomeForMonth = 0;


        double januarySpend = 0;
        double februarySpend = 0;
        double marchSpend = 0;
        double aprilSpend = 0;
        double maySpend = 0;
        double juneSpend = 0;
        double julySpend = 0;
        double augustSpend = 0;
        double septemberSpend = 0;
        double octoberSpend = 0;
        double novemberSpend = 0;
        double decemberSpend = 0;

        double[] monthsSpend = {januarySpend, februarySpend, marchSpend, aprilSpend,
                maySpend, juneSpend, julySpend, augustSpend, septemberSpend, octoberSpend,
                novemberSpend, decemberSpend};


        double januaryIncome = 0;
        double februaryIncome = 0;
        double marchIncome = 0;
        double aprilIncome = 0;
        double mayIncome = 0;
        double juneIncome = 0;
        double julyIncome = 0;
        double augustIncome = 0;
        double septemberIncome = 0;
        double octoberIncome = 0;
        double novemberIncome = 0;
        double decemberIncome = 0;

        double[] monthIncome = {januaryIncome, februaryIncome, marchIncome, aprilIncome,
                mayIncome, juneIncome, julyIncome, augustIncome, septemberIncome, octoberIncome,
                novemberIncome, decemberIncome};

        try {

            for (int i = 0; i < monthsSpend.length; i++) {
                monthsSpend[i] = totalSpendDAO.getTotalSpendByMonth(userId, i+1);
                if (i+1 == month)
                    totalSpendForMonth = monthsSpend[i];
            }


            for (int currentMonth = 0; currentMonth < monthIncome.length; currentMonth++) {
                monthIncome[currentMonth] = totalSpendDAO.getTotalIncomeByMonth(userId, currentMonth+1);
                if (currentMonth+1 == month)
                    totalIncomeForMonth = monthIncome[currentMonth];
            }


            response.put("januarySpend",  monthsSpend[0]);
            response.put("februarySpend", monthsSpend[1]);
            response.put("marchSpend", monthsSpend[2]);
            response.put("aprilSpend", monthsSpend[3]);
            response.put("maySpend", monthsSpend[4]);
            response.put("juneSpend", monthsSpend[5]);
            response.put("julySpend", monthsSpend[6]);
            response.put("augustSpend", monthsSpend[7]);
            response.put("septemberSpend", monthsSpend[8]);
            response.put("octoberSpend", monthsSpend[9]);
            response.put("novemberSpend", monthsSpend[10]);
            response.put("decemberSpend", monthsSpend[11]);

            response.put("test",augustIncome);

            response.put("totalSpendForMonth", totalSpendForMonth);

            response.put("januaryIncome",  monthIncome[0]);
            response.put("februaryIncome",  monthIncome[1]);
            response.put("marchIncome",  monthIncome[2]);
            response.put("aprilIncome",  monthIncome[3]);
            response.put("mayIncome",  monthIncome[4]);
            response.put("juneIncome",  monthIncome[5]);
            response.put("julyIncome",  monthIncome[6]);
            response.put("augustIncome",  monthIncome[7]);
            response.put("septemberIncome",  monthIncome[8]);
            response.put("octoberIncome",  monthIncome[9]);
            response.put("novemberIncome",  monthIncome[10]);
            response.put("decemberIncome",  monthIncome[11]);

            response.put("totalIncomeForMonth", totalIncomeForMonth);

            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            return response.put("status","ERROR");
        }

    }


    /**
     * Getting all the monthly spend per each category
     * @param categories JSON object that represent the request of all categories
     * @return the amount spend of each category
     * @throws CostManagementException
     */
    @GET
    @Consumes({"application/json"})
    public JSONObject getAllCategories(String categories) throws CostManagementException {

        JSONObject response = new JSONObject(categories);
        try {
            int userId = (int) response.get("currentUser");

            double shopping = spendDAO.getSpendByCategory(userId, "Shopping");
            double transport = spendDAO.getSpendByCategory(userId, "Transport");
            double restaurant = spendDAO.getSpendByCategory(userId, "Restaurant");
            double health = spendDAO.getSpendByCategory(userId, "Health");
            double family = spendDAO.getSpendByCategory(userId, "Family");
            double groceries = spendDAO.getSpendByCategory(userId, "Groceries");
            double leisure = spendDAO.getSpendByCategory(userId, "Leisure");
            double government = spendDAO.getSpendByCategory(userId, "Government");
            double food = spendDAO.getSpendByCategory(userId, "Food");

            response.put("Shopping", shopping);
            response.put("Transport", transport);
            response.put("Restaurant", restaurant);
            response.put("Health", health);
            response.put("Family", family);
            response.put("Groceries", groceries);
            response.put("Leisure", leisure);
            response.put("Government", government);
            response.put("Food", food);

            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
             response.put("status","ERROR");
            return response.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Getting spend amount for currently month
     * @param spend JSON object the represent the current user and the selected month
     * @return the spend amount of the current month
     */
    @GET
    @Path("/getspendformonth")
    @Consumes({"application/json"})
    public JSONObject getSpendForMonth(JSONObject spend) {

        JSONObject totalSpend = new JSONObject();
        try {
            User user = (User) spend.get("currentUser");
            int month = (Integer) spend.get("month");

            double spendForMonth = totalSpendDAO.getTotalSpendByMonth(user.getId(), month);

            totalSpend.put("spendForMonth", spendForMonth);
            return totalSpend.put("status", "OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            totalSpend.put("status","ERROR");
            return totalSpend.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Getting the income amount of the current month
     * @param income JSON object the represent the current user and the selected month
     * @return income amount for the current month
     * @throws CostManagementException
     */
    @GET
    @Path("/monthincome")
    @Consumes({"application/json"})
    public JSONObject getIncomeForMonth(JSONObject income)  {
        JSONObject totalIncome = new JSONObject();
        try {
            User user = (User) income.get("currentUser");
            int month = (Integer) income.get("month");

            double incomeForMonth = totalSpendDAO.getTotalIncomeByMonth(user.getId(), month);

            totalIncome.put("incomeForMonth", incomeForMonth);
            return totalIncome.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            totalIncome.put("status","ERROR");
            return totalIncome.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Getting total costs amount for the current month
     * @param amount JSON object the represent the current user and selected month
     * @return total costs amount for the current month
     */
    @GET
    @Path("/totalamount")
    @Consumes({"application/json"})
    public JSONObject getTotalAmount(JSONObject amount) {
        JSONObject totalAmount = new JSONObject();
        try {
            User user = (User) amount.get("currentUser");
            int month = (Integer) amount.get("month");

            double totalAmountForMonth = totalSpendDAO.getTotalAmountByMonth(user.getId(), month);

            totalAmount.put("totalAmountForMonth", totalAmountForMonth);
            return totalAmount.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            totalAmount.put("status","ERROR");
            return totalAmount.put("errorMessage",e.getMessage());
        }
    }

}
