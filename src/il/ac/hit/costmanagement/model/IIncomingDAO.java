package il.ac.hit.costmanagement.model;

import il.ac.hit.costmanagement.exception.CostManagementException;
import il.ac.hit.costmanagement.dm.Incoming;

public interface IIncomingDAO {
    void addIncoming(Incoming incoming) throws CostManagementException;
    void deleteIncoming(int id) throws CostManagementException;
    double getIncomeByMonth(int id, int month) throws CostManagementException;
}
