package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IIncomingDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import il.ac.hit.costmanagement.model.ITotalSpend;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class HomeService {

    IIncomingDAO dao = CostManagementDAO.getInstance();
    ISpendDAO spendDAO = CostManagementDAO.getInstance();
    ITotalSpend totalSpendDAO = CostManagementDAO.getInstance();

    @GET
    @Consumes({"application/json"})
    public JSONObject getAllYearCosts(JSONObject response) throws CostManagementException {

        User user = (User) response.get("currentUser");
        int month = (int) response.get("month");
        ISpendDAO spendDAO = CostManagementDAO.getInstance();
        IIncomingDAO incomingDAO = CostManagementDAO.getInstance();

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

                for (int currentMonth = 1; currentMonth <= monthsSpend.length; currentMonth++) {
                    monthsSpend[currentMonth] = spendDAO.getSpendByMonth(user.getId(), currentMonth);
                    if (currentMonth == month)
                        totalSpendForMonth = monthsSpend[currentMonth];
                }


                for (int currentMonth = 1; currentMonth <= monthIncome.length; currentMonth++) {
                    monthIncome[currentMonth] = incomingDAO.getIncomeByMonth(user.getId(), currentMonth);
                    if (currentMonth == month)
                        totalIncomeForMonth = monthIncome[currentMonth];
                }


                response.put("januarySpend", januarySpend);
                response.put("februarySpend", februarySpend);
                response.put("marchSpend", marchSpend);
                response.put("aprilSpend", aprilSpend);
                response.put("maySpend", maySpend);
                response.put("juneSpend", juneSpend);
                response.put("julySpend", julySpend);
                response.put("augustSpend", augustSpend);
                response.put("septemberSpend", septemberSpend);
                response.put("octoberSpend", octoberSpend);
                response.put("novemberSpend", novemberSpend);
                response.put("decemberSpend", decemberSpend);

                response.put("totalSpendForMonth", totalSpendForMonth);

                response.put("januaryIncome", januaryIncome);
                response.put("februaryIncome", februaryIncome);
                response.put("marchIncome", marchIncome);
                response.put("aprilIncome", aprilIncome);
                response.put("mayIncome", mayIncome);
                response.put("juneIncome", juneIncome);
                response.put("julyIncome", julyIncome);
                response.put("augustIncome", augustIncome);
                response.put("septemberIncome", septemberIncome);
                response.put("octoberIncome", octoberIncome);
                response.put("novemberIncome", novemberIncome);
                response.put("decemberIncome", decemberIncome);

                response.put("totalIncomeForMonth", totalIncomeForMonth);

                return response.put("status","OK");
            }
         catch (CostManagementException e){
                e.printStackTrace();
                 return response.put("status","ERROR");


           /* double januarySpend =  (double)costsForAllYears.get("januarySpend");
            double februarySpend = (double)costsForAllYears.get("februarySpend");
            double marchSpend = (double)costsForAllYears.get("marchSpend");
            double aprilSpend = (double)costsForAllYears.get("aprilSpend");
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
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");
            costsForAllYears.get("januarySpend");*/
            //int month = (Integer)spend.get("month");

            //double spendForMonth = totalSpendDAO.getTotalSpendByMonth(user.getId(),month);

/*        JSONObject totalSpend = new JSONObject();
        return totalSpend.put("spendForMonth", spendForMonth);*/
        }

    }


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

        @GET
        @Consumes({"application/json"})
        public JSONObject getSpendForMonth(JSONObject spend) throws CostManagementException {

            User user = (User) spend.get("currentUser");
            int month = (Integer)spend.get("month");

            double spendForMonth = totalSpendDAO.getTotalSpendByMonth(user.getId(),month);

            JSONObject totalSpend = new JSONObject();
            return totalSpend.put("spendForMonth", spendForMonth);
        }

        @GET
        @Consumes({"application/json"})
        public JSONObject getIncomeForMonth(JSONObject income) throws CostManagementException {

            User user = (User) income.get("currentUser");
            int month = (Integer)income.get("month");

            double incomeForMonth = totalSpendDAO.getTotalIncomeByMonth(user.getId(),month);

            JSONObject totalIncome = new JSONObject();
            return totalIncome.put("incomeForMonth", incomeForMonth);
        }

        @GET
        @Consumes({"application/json"})
        public JSONObject getTotalAmount(JSONObject amount) throws CostManagementException {

            User user = (User) amount.get("currentUser");
            int month = (Integer)amount.get("month");

            double totalAmountForMonth = totalSpendDAO.getTotalAmountByMonth(user.getId(),month);

            JSONObject totalAmount = new JSONObject();
            return totalAmount.put("totalAmountForMonth", totalAmountForMonth);
        }

    }
