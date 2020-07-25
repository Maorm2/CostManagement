package il.ac.hit.costmanagement.driver;

import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.model.IIncomingDAO;
import il.ac.hit.costmanagement.model.ISpendDAO;
import il.ac.hit.costmanagement.model.ITotalSpend;
import il.ac.hit.costmanagement.model.IUserDAO;
import il.ac.hit.costmanagement.model.CostManagementDAO;

import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws  CostManagementException{
        actionHandler();
    }

    public static void actionHandler() throws CostManagementException {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        IUserDAO dao = null;
        IIncomingDAO incomingDAO = null;
        ISpendDAO spendDAO = null;
        ITotalSpend totalSpend = null;
        int action;

        while (true) {
        System.out.println("\nSelect the action:\n 1.Exit \n 2.Register User \n " +
                "3.User Authentication \n 4.Add Incoming \n 5.Remove Incoming \n 6.Add Spend" +
                "\n 7.Remove Spend \n 8.Get total spend by month \n 9.Get total spend by year" +
                "\n 10.Get spend by category \n 11.Get Income by month \n 12. Get Spend by month" +
                "\n Your choice?");
        action = scanner.nextInt();
            switch(action) {
                case 1:
                    scanner.close();
                    System.exit(0);
                    break;
                case 2:
                    dao = CostManagementDAO.getInstance();
                    User user = new User("nati@gmail.com","123456",2);
                    dao.registerUser(user);
                    break;
                case 3:
                    dao = CostManagementDAO.getInstance();
                    //User user1 = new User("maor@gmail.com","123456",1000);
                    dao.userAuthentication("maor@gmail.com","123456");
                    break;
                case 4:
                    incomingDAO = CostManagementDAO.getInstance();
                    Incoming incoming = new Incoming(1,1000.5,new Date(Calendar.getInstance().getTimeInMillis())
                            ,true, "salary",0);
                    incomingDAO.addIncoming(incoming);
                    break;
                case 5:
                    incomingDAO = CostManagementDAO.getInstance();
                    incomingDAO.deleteIncoming(1036);
                    break;
                case 6:
                    spendDAO = CostManagementDAO.getInstance();
                    Spend spend = new Spend(1,3250.5,new Date(Calendar.getInstance().getTimeInMillis()),
                            "Government",false, "KFC",0);
                    spendDAO.addSpend(spend);
                    break;
                case 7:
                    spendDAO = CostManagementDAO.getInstance();
                    spendDAO.deleteSpend(1332);
                    break;
                case 8:
                    totalSpend = CostManagementDAO.getInstance();
                    totalSpend.getTotalSpendByMonth(1,10);
                    break;
                case 9:
                    totalSpend = CostManagementDAO.getInstance();
                    totalSpend.getTotalSpendByYear(1,2020);
                    break;
                case 10:
                    spendDAO = CostManagementDAO.getInstance();
                    spendDAO.getSpendByCategory(new User("maor@gmail.com","123456",1),"Government");
                    break;
                case 11:
                    incomingDAO = CostManagementDAO.getInstance();
                    double amount = incomingDAO.getIncomeByMonth(1,7);
                    System.out.println("Amount: " + amount);
                    break;
                case 12:
                    spendDAO = CostManagementDAO.getInstance();
                    double amount1;
                     //amount1 = spendDAO.getSpendByMonth(1,2);
                     amount1 = spendDAO.getSpendByMonth(1,3);
                    // amount1 = spendDAO.getSpendByMonth(1,7);
                    // amount1 = spendDAO.getSpendByMonth(1,4);
                    // amount1 = spendDAO.getSpendByMonth(1,10);
                    // amount1 = spendDAO.getSpendByMonth(1,5);
                   //  amount1 = spendDAO.getSpendByMonth(1,11);
                  //    amount1 = spendDAO.getSpendByMonth(1,7);
                   //  amount1 = spendDAO.getSpendByMonth(1,6);
                   //  amount1 = spendDAO.getSpendByMonth(1,12);
                   //  amount1 = spendDAO.getSpendByMonth(1,8);
                    //  amount1 = spendDAO.getSpendByMonth(1,9);





                    System.out.println("Amount: " + amount1);
                    break;

                default:
                    System.out.println("Invalid action");
            }
        }
    }
}
