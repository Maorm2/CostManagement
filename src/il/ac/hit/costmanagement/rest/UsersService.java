package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UsersService {

    IUserDAO dao = CostManagementDAO.getInstance();

    @POST
    @Path("/authentication")
    @Consumes({"application/json"})
    public JSONObject checkAuthentication(JSONObject user) throws CostManagementException {
        String email = (String)user.get("email");
        String password = (String)user.get("password");
        JSONObject userAuthentication = new JSONObject();
        return userAuthentication.put("isLoginSucceed",dao.userAuthentication(email,password));
    }

    @GET
    @Path("/user")
    @Consumes({"application/json"})
    public JSONObject getUser(JSONObject email) throws CostManagementException{
        String user = (String) email.get("email");
        JSONObject currentUser = new JSONObject();
        return currentUser.put("currentUser",dao.getCurrentUser(user));
    }

    @POST
    @Path("/register")
    @Produces("text/html")
    @Consumes({"application/json"})
    public JSONObject registerUser(JSONObject jsonUser) throws CostManagementException {
        User user = (User) jsonUser.get("user");
        JSONObject userRegistration = new JSONObject();
        return userRegistration.put("isRegistrationSucceed",dao.registerUser(user));
    }


    /*************/


    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String registerUser(
        @DefaultValue("maor") @QueryParam("test") String name)
    {
        return "hELLO EOTLF " + name;
    }


    /******************** */
}
