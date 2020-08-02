package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IIncomingDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import org.json.JSONObject;


import javax.ws.rs.*;

public class ActionsService {

    ISpendDAO spendDAO = CostManagementDAO.getInstance();
    IIncomingDAO incomingDAO = CostManagementDAO.getInstance();

    @POST
    @Path("/addSpend")
    @Produces("text/html")
    @Consumes({"application/json"})
    public JSONObject addSpend(JSONObject newSpend) {
        JSONObject response = new JSONObject();
        try {
            Spend spend = (Spend) newSpend.get("newSpend");
            spendDAO.addSpend(spend);
            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            return response.put("status","ERROR");

        }
    }

    @POST
    @Path("/addIncome")
    @Produces("text/html")
    @Consumes({"application/json"})
    public JSONObject addIncome(JSONObject newIncome) {
        JSONObject response = new JSONObject();
        try {
            Incoming income = (Incoming) newIncome.get("newIncome");
            incomingDAO.addIncoming(income);
            return response.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            return response.put("status","ERROR");

        }
    }

}
