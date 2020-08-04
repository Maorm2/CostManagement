package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import il.ac.hit.costmanagement.model.ITotalSpend;
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
     * @param response JSON object that represent data from request and the data of response
     * @return JSON object that represent the costs of each month of the currently year
     */
    @GET
    @Path("/yearcosts")
    @Consumes({"application/json"})
    public JSONObject getAllYearCosts(JSONObject response) {

        User user = (User) response.get("currentUser");
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
                    monthsSpend[i] = totalSpendDAO.getTotalSpendByMonth(user.getId(), i+1);
                    if (i+1 == month)
                        totalSpendForMonth = monthsSpend[i];
                }


                for (int currentMonth = 0; currentMonth < monthIncome.length; currentMonth++) {
                    monthIncome[currentMonth] = totalSpendDAO.getTotalIncomeByMonth(user.getId(), currentMonth+1);
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
        public JSONObject getAllCategories(JSONObject categories) throws CostManagementException {

            JSONObject response = new JSONObject();
            try {
                User user = (User) categories.get("currentUser");

                double shopping = spendDAO.getSpendByCategory(user, "Shopping");
                double transport = spendDAO.getSpendByCategory(user, "Transport");
                double restaurant = spendDAO.getSpendByCategory(user, "Restaurant");
                double health = spendDAO.getSpendByCategory(user, "Health");
                double family = spendDAO.getSpendByCategory(user, "Family");
                double groceries = spendDAO.getSpendByCategory(user, "Groceries");
                double leisure = spendDAO.getSpendByCategory(user, "Leisure");
                double government = spendDAO.getSpendByCategory(user, "Government");
                double food = spendDAO.getSpendByCategory(user, "Food");

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
                return response.put("status","ERROR");
            }
        }

    /**
     * Getting spend amount for currently month
     * @param spend JSON object the represent the current user and the selected month
     * @return the spend amount of the current month
     * @throws CostManagementException
     */
        @GET
        @Path("/getspendformonth")
        @Consumes({"application/json"})
        public JSONObject getSpendForMonth(JSONObject spend) throws CostManagementException {

            User user = (User) spend.get("currentUser");
            int month = (Integer)spend.get("month");

            double spendForMonth = totalSpendDAO.getTotalSpendByMonth(user.getId(),month);

            JSONObject totalSpend = new JSONObject();
            return totalSpend.put("spendForMonth", spendForMonth);
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
        public JSONObject getIncomeForMonth(JSONObject income) throws CostManagementException {

            User user = (User) income.get("currentUser");
            int month = (Integer)income.get("month");

            double incomeForMonth = totalSpendDAO.getTotalIncomeByMonth(user.getId(),month);

            JSONObject totalIncome = new JSONObject();
            return totalIncome.put("incomeForMonth", incomeForMonth);
        }

    /**
     * Getting total costs amount for the current month
     * @param amount JSON object the represent the current user and selected month
     * @return total costs amount for the current month
     * @throws CostManagementException
     */
        @GET
        @Path("/totalamount")
        @Consumes({"application/json"})
        public JSONObject getTotalAmount(JSONObject amount) throws CostManagementException {

            User user = (User) amount.get("currentUser");
            int month = (Integer)amount.get("month");

            double totalAmountForMonth = totalSpendDAO.getTotalAmountByMonth(user.getId(),month);

            JSONObject totalAmount = new JSONObject();
            return totalAmount.put("totalAmountForMonth", totalAmountForMonth);
        }

    }
