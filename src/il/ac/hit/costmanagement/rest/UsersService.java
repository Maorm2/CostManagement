package il.ac.hit.costmanagement.rest;

import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersService {

    IUserDAO dao = CostManagementDAO.getInstance();

    /**
     * Checking user authentication
     * @param user the current user that has login
     * @return true if succeed , otherwise false
     * @throws CostManagementException
     */
    @POST
    @Path("/authentication")
    @Consumes({"application/json"})
    public JSONObject checkAuthentication(JSONObject user) throws CostManagementException {
        String email = (String)user.get("email");
        String password = (String)user.get("password");
        JSONObject userAuthentication = new JSONObject();
        return userAuthentication.put("isLoginSucceed",dao.userAuthentication(email,password));
    }

    /**
     * Getting the current user for making actions in the program
     * @param email JSON object that represent the user
     * @return JSON object of the user
     * @throws CostManagementException
     */
    @GET
    @Path("/user")
    @Consumes({"application/json"})
    public JSONObject getUser(JSONObject email) throws CostManagementException{
        String user = (String) email.get("email");
        JSONObject currentUser = new JSONObject();
        return currentUser.put("currentUser",dao.getCurrentUser(user));
    }

    /**
     * Register a new user
     * @param jsonUser JSON object that represent the user name and password of the register page
     * @return true if succeed , otherwise false
     * @throws CostManagementException
     */
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
    @Produces("text/html")
    @Consumes({"application/json"})
    public Response registerUser(@DefaultValue("user") String user1) throws CostManagementException {
        User user = dao.getCurrentUser("maor22@gmail.com");
        return Response.ok().entity("Hello world" + user).build();
    }


    /******************** */
}
