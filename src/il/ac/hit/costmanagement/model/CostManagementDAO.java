package il.ac.hit.costmanagement.model;

import il.ac.hit.costmanagement.dm.Incoming;
import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.TotalSpend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CostManagementDAO implements IIncomingDAO,
        ISpendDAO, ITotalSpend, IUserDAO
{
    private Session session;
    private SessionFactory factory;
    private Query hql;
    private String query = "from Incoming where transactionId = :id";
    private double spend;
    private double income;
    private double total = 0;
    private TotalSpend totalSpend;
    private static CostManagementDAO instance = null;
    private int transactionId;
    private int month = new Date(Calendar.getInstance().getTimeInMillis()).toLocalDate().getMonth().getValue();
    private int year =  new Date(Calendar.getInstance().getTimeInMillis()).toLocalDate().getYear();

    private CostManagementDAO()  {
        factory = new Configuration().configure().buildSessionFactory();
        session = factory.openSession();
        session.beginTransaction();
    }

    public static CostManagementDAO getInstance()  {

        return instance == null ? instance = new CostManagementDAO() : instance;
    }


    @Override
    public void addIncoming(Incoming incoming) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            session.save(incoming);
            session.getTransaction().commit();

            if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            addIncomeToTotalSpend(incoming);

            System.out.println(incoming.toString() +"\nAction succeed\n");
        }

        catch (HibernateException | CostManagementException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
    }


    @Override
    public void deleteIncoming(int id) throws CostManagementException {

        int rowsAffected;
        Incoming incoming;

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            query = "from Incoming where transactionId = :id";
            hql = session.createQuery(query).setParameter("id", id);
            List<Incoming> incomingList = hql.list();


            query = "delete Incoming where transactionId = :id";
            hql = session.createQuery(query).setParameter("id",id);
            rowsAffected =  hql.executeUpdate();

            if(rowsAffected == 0)
                throw new CostManagementException("There is no transaction with that id",
                        new Throwable("Invalid id"));

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            if(!incomingList.isEmpty()) {
                incoming = incomingList.get(0);
                deleteIncomeFromTotalSpend(incoming);
            }


            System.out.println("\nAction succeed\n");
        }

        catch (HibernateException | CostManagementException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
    }

    @Override
    public double getIncomeByMonth(int id, int month) throws CostManagementException {

        Incoming incoming;

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }


        try{
            hql =session.createQuery("from Incoming where id = :id");
            hql.setParameter("id", id);
            List<Incoming> incomingList = hql.list();

            if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }


            if(incomingList.isEmpty()) {
                return 0;
            }
            else {
                incoming = incomingList.get(0);
                Iterator<Incoming> iterator = incomingList.iterator();

                while (iterator.hasNext()) {
                    if (month == incoming.getDate().toLocalDate().getMonth().getValue()) {
                        income = income + incoming.getAmount();
                    }
                    incoming = iterator.next();
                }
                return income;
            }
        }

        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            income = 0;
            if(session!=null)
                session.close();
        }
    }

    @Override
    public void addSpend(Spend spend) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            session.save(spend);
            session.getTransaction().commit();

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            addSpendToTotalSpend(spend);

            System.out.println(spend.toString() + "\nAction succeed\n");
        }

        catch (HibernateException | CostManagementException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
    }

    @Override
    public void deleteSpend(int id) throws CostManagementException {

        Spend spend;
        int rowsAffected;

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            query = "from Spend where transactionId = :id";
            hql = session.createQuery(query).setParameter("id", id);
            List<Spend> spendList = hql.list();


            query = "delete Spend where transactionId = :id";
            hql = session.createQuery(query).setParameter("id", id);
            rowsAffected =  hql.executeUpdate();

            if(rowsAffected == 0)
                throw new CostManagementException("There is no transaction with that id",
                        new Throwable("Invalid id"));

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            if(!spendList.isEmpty()) {
                spend = spendList.get(0);
                deleteSpendFromTotalSpend(spend);
            }

            System.out.println("\nAction succeed\n");
        }

        catch (HibernateException | CostManagementException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }

    }

    @Override
    public double getSpendByCategory(User user, String category) throws CostManagementException {

        int userId = user.getId();
        double amountCategory = 0;

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try{
            hql = session.createQuery("from Spend where category = :category and id = :id");
            hql.setParameter("category", category).setParameter("id",userId);
            List<Spend> spendList = hql.list();


            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            if(spendList.isEmpty())
               return 0;

            else{
                for(Spend spend : spendList){
                    if(spend.getDate().toLocalDate().getMonth().getValue()== month)
                        amountCategory = amountCategory + spend.getAmount();
                }
            }

            System.out.println(" Costs for month " + month + ":\n are: " + amountCategory +" for category: " + category);
        }

        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
        return amountCategory;
    }

    @Override
    public double getSpendByMonth(int id, int month) throws CostManagementException {

        Spend spendTable;

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try{
            hql =session.createQuery("from Spend where id = :id");
            hql.setParameter("id", id);
            List<Spend> spendList = hql.list();

            if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }


            if(spendList.isEmpty()) {
                return 0;
            }
            else {
                spendTable = spendList.get(0);
                Iterator<Spend> iterator = spendList.iterator();

                while (iterator.hasNext()) {
                    if (month == spendTable.getDate().toLocalDate().getMonth().getValue()) {
                        spend = spend + spendTable.getAmount();
                    }
                    spendTable = iterator.next();
                }
                return spend;
            }
        }

        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            spend = 0;
           if(session!=null)
                session.close();
        }
    }

    @Override
    public double getTotalSpendByMonth(int id, int month) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try{
            hql = session.createQuery("from TotalSpend where month = :month and id = :id");
            hql.setParameter("month", month).setParameter("id",id);
            List<TotalSpend> totalSpendList = hql.list();


            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            if(totalSpendList.isEmpty())
                throw new CostManagementException("The selected month is invalid",
                        new Throwable("No spends recorded for this month"));

            else{
                totalSpend = totalSpendList.get(0);
                spend = totalSpend.getAmountSpend();
                income = totalSpend.getAmountIncome();
                total = income - spend;
            }

            System.out.println(" Costs for month " + month + ":\n Spend amount: " +spend + "\n Income amount: " + income
                    + "\n Total: " + total);
        }

        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
            spend = 0;
            income = 0;
            total = 0;
        }

        return total;
    }

    @Override
    public double getTotalSpendByYear(int id, int year) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try{
            hql = session.createQuery("FROM TotalSpend where year = :year and id = :id");
            hql.setParameter("year", year).setParameter("id",id);
            List<TotalSpend> totalSpendList = hql.list();

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            if(totalSpendList.isEmpty())
                throw new CostManagementException("The selected year is invalid",
                        new Throwable("No spends recorded for this year"));

            else{
                Iterator<TotalSpend> iterator = totalSpendList.iterator();

                /* Retrieve from database all the rows for the selected year
                   and calculates all the spend and income for the selected year
                   and returns the total cost for this year. */

                while(iterator.hasNext()){
                    totalSpend = iterator.next();
                    spend = spend + totalSpend.getAmountSpend();
                    income = income + totalSpend.getAmountIncome();
                }
                total = income - spend;
            }

            System.out.println(" Costs for year " + year + ":\n Spend amount: " +spend + "\n Income amount: " + income
                    + "\n Total: " + total);
        }

        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }

        return total;
    }

    @Override
    public void addSpendToTotalSpend(Spend spend) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            transactionId = spend.getId();
           // session.beginTransaction();
            hql = session.createQuery("from TotalSpend where month = :month and id = :id");
            hql.setParameter("month", month).setParameter("id", transactionId);
            List<TotalSpend> totalSpendList = hql.list();

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }


             /* Check if there is already month with spend amount on db.
             if yes the list.size return value 1 else it returns value 0
             and then we create a new row for this month.
             Each row has specific month. */

            if (totalSpendList.isEmpty()) {
                totalSpend = new TotalSpend(spend.getId(), spend.getAmount(), 0, year, month);
                session.save(totalSpend);
                session.getTransaction().commit();


                if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                    session.getTransaction().rollback();
                    throw new CostManagementException("Action failed",
                            new Throwable("The transaction was not committed"));
                }
            } else {
                totalSpend = totalSpendList.get(0);
                double newAmount = spend.getAmount() + totalSpend.getAmountSpend();
                hql = session.createQuery("UPDATE TotalSpend spend SET spend.amountSpend = :newAmount WHERE spend.month = :month");
                hql.setParameter("newAmount", newAmount).setParameter("month", month);
                hql.executeUpdate();
            }

        }
        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
    }

    @Override
    public void addIncomeToTotalSpend(Incoming incoming) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            transactionId = incoming.getId();
            // session.beginTransaction();
            hql = session.createQuery("from TotalSpend where month = :month and id = :id");
            hql.setParameter("month", month).setParameter("id", transactionId);
            List<TotalSpend> totalSpendList = hql.list();


             /* Check if there is already month with spend amount on db.
             if yes the list.size return value 1 else it returns value 0
             and then we create a new row for this month.
             Each row has specific month. */

            if (totalSpendList.isEmpty()) {
                totalSpend = new TotalSpend(incoming.getId(), 0, incoming.getAmount(), year, month);
                session.save(totalSpend);
                session.getTransaction().commit();


                if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                    session.getTransaction().rollback();
                    throw new CostManagementException("Action failed",
                            new Throwable("The transaction was not committed"));
                }
            } else {
                totalSpend = totalSpendList.get(0);
                double newAmount = incoming.getAmount() + totalSpend.getAmountIncome();
                hql = session.createQuery("UPDATE TotalSpend spend SET spend.amountIncome = :newAmount WHERE spend.month = :month");
                hql.setParameter("newAmount", newAmount).setParameter("month", month);
                hql.executeUpdate();
            }
        }
        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }
    }

    @Override
    public void deleteSpendFromTotalSpend(Spend spend) throws CostManagementException {

        transactionId = spend.getId();

        int month = spend.getDate().toLocalDate().getMonth().getValue();

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            hql = session.createQuery("from TotalSpend where month = :month and id = :id");
            hql.setParameter("month", month).setParameter("id", transactionId);
            List<TotalSpend> totalSpendList = hql.list();

            totalSpend = totalSpendList.get(0);
            double newAmount = totalSpend.getAmountSpend() - spend.getAmount();
            hql = session.createQuery("UPDATE TotalSpend spend SET spend.amountSpend = :newAmount WHERE spend.month = :month");
            hql.setParameter("newAmount", newAmount).setParameter("month", month);
            hql.executeUpdate();

            if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }
        }
        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }

    }

    @Override
    public void deleteIncomeFromTotalSpend(Incoming incoming) throws CostManagementException {

        transactionId = incoming.getId();
        int month = incoming.getDate().toLocalDate().getMonth().getValue();

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {

            hql = session.createQuery("from TotalSpend where month = :month and id = :id");
            hql.setParameter("month", month).setParameter("id", transactionId);
            List<TotalSpend> totalSpendList = hql.list();

            totalSpend = totalSpendList.get(0);
            double newAmount = totalSpend.getAmountIncome() - incoming.getAmount();
            hql = session.createQuery("UPDATE TotalSpend spend SET spend.amountIncome = :newAmount WHERE spend.month = :month");
            hql.setParameter("newAmount", newAmount).setParameter("month", month);
            hql.executeUpdate();


            if (session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }
        }
        catch (HibernateException e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }

        finally {
            if(session!=null)
                session.close();
        }

    }

    @Override
    public boolean registerUser(User user) throws CostManagementException {

        String userName = user.getUserName();

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            hql = session.createQuery("from User where userName = :userName");
            hql.setParameter("userName", userName);
            List<User> userList = hql.list();

            /* Check if the user name is already exists in database
            by checking the size of the list. if the list is not empty
            there is a user with this user name and then we throw an exception.
            other wise we insert the user to database. */

            if(userList.isEmpty()){
                session.save(user);
                session.getTransaction().commit();
            }

            else{
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("User name is already exists",
                        new Throwable("The email that entered is already exists in DB"));
            }

            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            return true;

        } catch (HibernateException e) {
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }
         finally {
            if (session!=null){
                session.close();
            }
        }
    }

    @Override
    public boolean userAuthentication(String userName, String password) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {
            hql = session.createQuery("from User where userName = :userName");
            hql.setParameter("userName", userName);
            List<User> userList = hql.list();

            /* Check if the user name is already exists in database
            by checking the size of the list. if the list is not empty
            there is a user with this user name and then we check the password
            if the password are match - authentication succeed
            other wise authentication failed. */

            if(!userList.isEmpty()){
                String passFromDB = userList.get(0).getPassword();
                if(passFromDB.equals(password)){
                    System.out.println("Authentication succeed");
                    return true;
                }
                else{
                    if(session.getTransaction().isActive())
                        session.getTransaction().rollback();
                    throw new CostManagementException("Username or password is incorrect",
                            new Throwable("Password is incorrect"));
                }
            }

            else{
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Username or password is incorrect",
                        new Throwable("Wrong username"));
            }


        } catch (HibernateException e) {
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        }
        finally {
            if (session!=null){
                session.close();
            }
        }
    }

    @Override
    public User getCurrentUser(String userName) throws CostManagementException {

        if(!session.isOpen()){
            session = factory.openSession();
            session.beginTransaction();
        }

        try {

            hql = session.createQuery("from User where userName = :userName");
            hql.setParameter("userName", userName);
            List<User> userList = hql.list();

            /* Check if the user name is already exists in database
            by checking the size of the list. if the list is not empty
            there is a user with this user name and then we check the password
            if the password are match - authentication succeed
            other wise authentication failed. */

            if(userList.isEmpty()){
                    if(session.getTransaction().isActive())
                        session.getTransaction().rollback();
                    throw new CostManagementException("Username is incorrect",
                            new Throwable("The user name is not exists"));
                }
            if(session.getTransaction().getStatus() == TransactionStatus.FAILED_COMMIT) {
                if(session.getTransaction().isActive())
                    session.getTransaction().rollback();
                throw new CostManagementException("Action failed",
                        new Throwable("The transaction was not committed"));
            }

            return userList.get(0);

        } catch (HibernateException e) {
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new CostManagementException(e.getMessage());
        } finally {

            if (session!=null){
                session.close();
            }
        }
    }
}


