package il.ac.hit.costmanagement.exception;

public class CostManagementException extends Exception {

    public CostManagementException(String message) {
        super(message);
    }
    public CostManagementException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
