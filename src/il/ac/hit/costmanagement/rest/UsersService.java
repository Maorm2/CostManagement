package il.ac.hit.costmanagement.rest;


import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.CostManagementDAO;
import il.ac.hit.costmanagement.model.IUserDAO;
import jdk.nashorn.internal.parser.JSONParser;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.server.Uri;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Path("/users")
public class UsersService {

    IUserDAO dao = CostManagementDAO.getInstance();

    /**
     * Checking user authentication
     * @param jsonUser the current user that has login
     * @return true if succeed , otherwise false
     */
    @POST
    @Path("/authentication")
    @Produces("application/json")
    @Consumes("application/json")
    public JSONObject checkAuthentication(String jsonUser) {
        System.out.println("json user: " + jsonUser);
        JSONObject userAuthentication = new JSONObject(jsonUser);
        System.out.println("userAuthentication: " + userAuthentication);
        try {
            String email = (String) userAuthentication.get("email");
            String password = (String) userAuthentication.get("password");
            userAuthentication.put("isLoginSucceed", dao.userAuthentication(email, password));
            System.out.println("Arrive success backend end");
            return userAuthentication.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            userAuthentication.put("status","ERROR");
            System.out.println("Arrive backend error");
            return userAuthentication.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Getting the current user for making actions in the program
     * @param jsonUser JSON object that represent the user
     * @return JSON object of the user
     */
    @GET
    @Path("/user")
    @Produces("application/json")
    @Consumes("application/json")
    public JSONObject getUser(String jsonUser) {
        JSONObject currentUser = new JSONObject(jsonUser);
        System.out.println("get user: " + jsonUser);
        try {
            String userName = currentUser.getString("email");
            currentUser.put("currentUser", dao.getCurrentUser(userName));
            return currentUser.put("status","OK");
        }
        catch (CostManagementException e){
            e.printStackTrace();
            currentUser.put("status","ERROR");
            return currentUser.put("errorMessage",e.getMessage());
        }
    }

    /**
     * Register a new user
     * @param jsonUser JSON object that represent the user name and password of the register page
     * @return true if succeed , otherwise false
     */
    @POST
    @Path("/register")
    @Produces("application/json")
    @Consumes({"application/json"})
    public JSONObject registerUser(String jsonUser) {

        JSONObject userRegistration = new JSONObject(jsonUser);
        System.out.println("json user: " + jsonUser);

        try {
            String email = userRegistration.getString("email");
            String password = userRegistration.getString("password");
            int id = 0;
            User user = new User(email,password,id);

            userRegistration.put("isRegistrationSucceed", dao.registerUser(user));

            return userRegistration.put("status","OK");
          //  return Response.ok().entity("Hello world").header("Access-Control-Allow-Origin","*").build();
        }
        catch (CostManagementException e){
            e.printStackTrace();
            userRegistration.put("status","ERROR");
            return  userRegistration.put("errorMessage",e.getMessage());
            //return Response.ok().entity("Hello world").header("Access-Control-Allow-Origin","*").build();
        }
    }

    @POST
    @Path("/hello")
    @Produces("application/json")
    @Consumes("application/json")
    public String hello(@DefaultValue("user") JSONObject userJson) throws CostManagementException {
        //User user = dao.getCurrentUser("maor22@gmail.com");
        // Response.ok().header;
        System.out.println("hello" + userJson);
        //  System.out.println("length: " + userJson.length());
        if (userJson!=null) {
            System.out.println(userJson.toString());
            // String email = userJson.getString("email");
            // String password = userJson.getString("password");


            System.out.println(userJson.toString());
            //  boolean isSucceed = dao.registerUser(new User(email, password, 0));
        }
        // return Response.ok().entity("Hello world").header("Access-Control-Allow-Origin","*").build();
        return "ok";
        /*if(isSucceed)
        return userJson.put("status","OK");

        return userJson.put("status","ERROR");*/
    }



    @POST
    @Path("/test")
   // @Produces("application/json")
   // @Consumes("application/json")
    public Response reg(@DefaultValue("user") String userJson) throws CostManagementException {
        String result = " maor";
        System.out.println("Arrive test function1 " + userJson);
        //User user = dao.getCurrentUser("maor22@gmail.com");
        // Response.ok().header;
        // System.out.println("hello" + userJson);
        //  System.out.println("length: " + userJson.length());
       /* byte[] decodedBytes = Base64.getDecoder().decode(userJson);
        String decodedString = new String(decodedBytes);*/
      /*  try {
            result = java.net.URLDecoder.decode(userJson, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }*/

        JSONObject jsonObject = new JSONObject(userJson);
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        System.out.println("Email: " +email +" \nPassword: " + password);
        // JSONParser parser = new JSONParser(userJson,Glo,0);
     /*   if (userJson!=null) {
            System.out.println(userJson.toString());
            // String email = userJson.getString("email");
            // String password = userJson.getString("password");



            System.out.println(userJson.toString());
            //  boolean isSucceed = dao.registerUser(new User(email, password, 0));

        }*/
        /*   ClientResponse response = service.path("rest").path("vtn").path("addEmplyee").type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class,object.toString());*/
        jsonObject.put("test","123");
        System.out.println("Arrive test function2 " + userJson);
        System.out.println("decoded string: " + result);

        return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
        //return Response.ok().entity("Hello world").header("Access-Control-Allow-Origin","*").build();
        //if(isSucceed)
        //return jsonObject.put("status","OK");

       // return userJson.put("status","ERROR");
    }


    /******************** */
}
