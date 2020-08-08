
package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IIncomingDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import il.ac.hit.costmanagement.model.ITotalSpend;
import org.json.JSONObject;


import javax.persistence.Id;
import javax.ws.rs.*;
import java.sql.Date;
import java.util.Calendar;

@Path("/actions")
public class ActionsService {

    ISpendDAO spendDAO = CostManagementDAO.getInstance();
    IIncomingDAO incomingDAO = CostManagementDAO.getInstance();
    ITotalSpend totalSpendDAO = CostManagementDAO.getInstance();

    /**
     * Adding a new spend
     * @param newSpend an object that represent the new spend
     * @return JSON object response if the action succeed or not
     */
    @POST
    @Path("/addspend")
    @Produces("text/html")
    @Consumes({"application/json"})
    public JSONObject addSpend(String newSpend) {
        JSONObject response = new JSONObject(newSpend);

        try {
            int userId = response.getInt("userId");
            double amount = response.getDouble("amount");
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            String category = response.getString("category");
            boolean permanentSpend = (boolean) response.get("permanentSpend");
            String comment = response.getString("comment");

            Spend spend = new Spend(userId,amount,date,category,permanentSpend,comment,0);
            spendDAO.addSpend(spend);
            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
             response.put("status","ERROR");
            return response.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Adding a new income
     * @param newIncome an object the represent the new income
     * @return JSON object response if the action succeed or not
     */
    @POST
    @Path("/addincome")
    @Produces("text/html")
    @Consumes({"application/json"})
    public JSONObject addIncome(String newIncome) {
        JSONObject response = new JSONObject(newIncome);

        try {

            int userId = response.getInt("currentUser");
            double amount = response.getDouble("amount");
            Date date = new Date(Calendar.getInstance().getTimeInMillis());
            boolean permanentIncome = response.getBoolean("permanentIncome");
            String comment = response.getString("comment");
            Incoming income = new Incoming(userId,amount,date,permanentIncome,comment,0);
            incomingDAO.addIncoming(income);
            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
             response.put("status","ERROR");
            return response.put("errorMessage",e.getMessage());

        }
    }

    /**
     * Getting the total amount spend by selected month
     * @param selectedMonth the value of the month
     * @return total amount spend in the selected month
     */
    @GET
    @Path("/spend/month/{selectedmonth}")
    @Consumes({"application/json"})
    public JSONObject getTotalSpendBySelectedMonth(@PathParam("selectedmonth") String selectedMonth){
             JSONObject response = new JSONObject(selectedMonth);

            try{
                int month = response.getInt("month");
                User user = (User) response.get("currentUser");
                double spendByMonth = totalSpendDAO.getTotalSpendByMonth(user.getId(),month);
                response.put("spendByMonth",spendByMonth);
                return response.put("status","OK");
            }
            catch (CostManagementException e){
                e.printStackTrace();
                response.put("status","ERROR");
                return response.put("errorMessage",e.getMessage());
            }

    }

    /**
     * Getting the total amount income by selected month
     * @param selectedMonth the value of the month
     * @return total amount income for selected month
     */
    @GET
    @Path("/income/month/{selectedmonth}")
    @Consumes({"application/json"})
    public JSONObject getTotalIncomeBySelectedMonth(@PathParam("selectedmonth") String selectedMonth){
        JSONObject response = new JSONObject(selectedMonth);

        try{
            int month = response.getInt("month");
            User user = (User) response.get("currentUser");
            double incomeByMonth = totalSpendDAO.getTotalIncomeByMonth(user.getId(),month);
            response.put("incomeByMonth",incomeByMonth);
            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            response.put("status","ERROR");
            return response.put("errorMessage",e.getMessage());
        }

    }
}
