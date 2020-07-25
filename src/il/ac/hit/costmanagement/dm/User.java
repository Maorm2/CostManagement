package il.ac.hit.costmanagement.dm;

import javax.persistence.Id;

public class User {

    @Id
    private int id;
    private String userName;
    private String password;


    public User() {
    }

    public User(String userName, String password, int id) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }


    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}