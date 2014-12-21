package learn.ejb;

import javax.ejb.Stateful;

@Stateful
public class StatefulServiceBean implements StatefulService {
    private String name;
    private double balance;

    public StatefulServiceBean() {
        name = IdGenerator.getRandomIdentifier();
        balance = Math.ceil(Math.random() * 10000);
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public String printAccountDetails() {
        return "Account [name=" + name + ", balance=" + balance + "]";
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
