package il.ac.hit.costmanagement.model;

import il.ac.hit.costmanagement.dm.Spend;
import il.ac.hit.costmanagement.dm.User;
import il.ac.hit.costmanagement.exception.CostManagementException;


public interface ISpendDAO {
    void addSpend(Spend spend) throws CostManagementException;
    void deleteSpend(int id) throws CostManagementException;
    double getSpendByCategory(User user, String category) throws CostManagementException;
    double getSpendByMonth(int id, int month) throws CostManagementException;
}
