package learn.ejb;

import javax.ejb.Remote;

@Remote
public interface StatefulService {
    void deposit(double amount);
    String printAccountDetails();
    public String getName();
    public void setName(String name);
    public double getBalance();
    public void setBalance(double balance);
}
