
package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IIncomingDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import org.json.JSONObject;


import javax.ws.rs.*;

@Path("/actions")
public class ActionsService {

    ISpendDAO spendDAO = CostManagementDAO.getInstance();
    IIncomingDAO incomingDAO = CostManagementDAO.getInstance();


    /**
     * Adding a new spend
     * @param newSpend an object that represent the new spend
     * @return JSON object response if the action succeed or not
     */
    @POST
    @Path("/addspend")
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

    /**
     * Adding a new income
     * @param newIncome an object the represent the new income
     * @return JSON object response if the action succeed or not
     */
    @POST
    @Path("/addincome")
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
